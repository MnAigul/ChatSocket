'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
const blockClick = document.querySelector('.d');
var chatMenu = document.querySelector('#chatMenu');


var chatRooms = new Set();
var selectedChat;
var stompClient = null;
var user = {username: null, status: 'ACTIVE'}

var colors = [
  '#2196F3', '#32c787', '#00BCD4', '#ff5652', '#ffc107', '#ff85af', '#FF9800',
  '#39bbb0'
];

function connect(event) {
  user.username = document.querySelector('#name').value.trim();
  var chatRoomName = document.querySelector('#chatRoomName').value.trim();
  if (!chatRooms.has(chatRoomName)) {
    chatRooms.add(chatRoomName);
  }
  fetch('/chatrooms')
      .then(response => response.json())
      .then(data => {
        data.forEach((elem) => {
          chatRooms.add(elem);
        })
        for (var chatRoom of chatRooms) {
          var li = document.createElement('li');
          li.innerHTML = `<a href="#" data-room="${chatRoom}">${chatRoom}</a>`;
          chatMenu.appendChild(li);
        }
      })
      .catch(error => {
        console.log('Error fetching chat rooms:', error);
      });
  if (user.username && chatRoomName) {
    selectedChat = chatRoomName;

    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
  }
  event.preventDefault();
}


function onConnected() {  // TODO: add check to one user have one name
  // alert('OK');
  fetch('/chatrooms/getUsers/' + selectedChat + '/' + user.username)
      .then(response => response.json())
      .then(data => {
        if (data === true) {
          alert('username exists in chatroom');
          window.location.reload();

        } else {
          stompClient.send(
              '/app/chat.addUser/' + selectedChat, {},
              JSON.stringify({sender: user.username, type: 'JOIN'}))
          connectingElement.classList.add('hidden');
        }
      })
  if (!subscription)
  subscription =
      stompClient.subscribe('/topic/' + selectedChat, onMessageReceived);
}


function onError(error) {
  connectingElement.textContent =
      'Could not connect to WebSocket server. Please refresh this page to try again!';
  connectingElement.style.color = 'red';
}


function sendMessage(event) {
  var messageContent = messageInput.value.trim();
  if (messageContent && stompClient) {
    var chatMessage = {
      sender: user.username,
      content: messageInput.value,
      type: 'CHAT'
    };
    stompClient.send(
        '/app/chat.sendMessage/' + selectedChat, {},
        JSON.stringify(chatMessage));
    messageInput.value = '';
    // }
  }
  event.preventDefault();
}


function onMessageReceived(payload) {
  var message = JSON.parse(payload.body);
  var messageElement = document.createElement('li');
  if (message.type === 'JOIN') {
    messageElement.classList.add('event-message');
    message.content = message.sender + ' joined!';
  } else if (message.type === 'LEAVE') {
    messageElement.classList.add('event-message');
    message.content = message.sender + ' left!';
  } else {
    //our messanges in right side
    if (message.sender == user.username) {
      messageElement.classList.add('chat-message');
      messageElement.style.textAlign = 'right';

      var avatarElement = document.createElement('i');
      var avatarText = document.createTextNode(message.sender[0]);
      avatarElement.appendChild(avatarText);
      avatarElement.style.backgroundColor = getAvatarColor(message.sender);
  
      messageElement.appendChild(avatarElement);

      var usernameElement = document.createElement('span');
      var usernameText = document.createTextNode(message.sender);
      usernameElement.appendChild(usernameText);

      messageElement.appendChild(usernameElement);
      //other messanges in left side
    } else {
      messageElement.classList.add('chat-message');

      var avatarElement = document.createElement('i');
      var avatarText = document.createTextNode(message.sender[0]);
      avatarElement.appendChild(avatarText);
      avatarElement.style['background-color'] = getAvatarColor(message.sender);

      messageElement.appendChild(avatarElement);

      var usernameElement = document.createElement('span');
      var usernameText = document.createTextNode(message.sender);
      usernameElement.appendChild(usernameText);

      messageElement.appendChild(usernameElement);
    }
  }

  var textElement = document.createElement('p');
  var messageText = document.createTextNode(message.content);
  textElement.appendChild(messageText);

  messageElement.appendChild(textElement);

  messageArea.appendChild(messageElement);
  messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
  var hash = 0;
  for (var i = 0; i < messageSender.length; i++) {
    hash = 31 * hash + messageSender.charCodeAt(i);
  }
  var index = Math.abs(hash % colors.length);
  return colors[index];
}


usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
chatMenu.addEventListener('click', function(e) {
  e.preventDefault();
  const room = e.target.getAttribute('data-room');
  switchChatRoom(room);
}, true);

function showOldMessanges() {
  fetch('/chatrooms/messages/' + selectedChat)
      .then(response => response.json())
      .then(
          messages => {messages.forEach((message) => {
            var messageElement = document.createElement('li');
            messageElement.classList.add('chat-message');

            var avatarElement = document.createElement('i');
            var avatarText = document.createTextNode(message.sender[0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] =
                getAvatarColor(message.sender);

            messageElement.appendChild(avatarElement);

            var usernameElement = document.createElement('span');
            var usernameText = document.createTextNode(message.sender);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
            var textElement = document.createElement('p');
            var messageText = document.createTextNode(message.content);
            textElement.appendChild(messageText);

            messageElement.appendChild(textElement);

            messageArea.appendChild(messageElement);
            messageArea.scrollTop = messageArea.scrollHeight;
          })

          })
}
var subscription = null;
function switchChatRoom(room) {
  if (selectedChat === room) {
    console.log('Already subscribed to chat room:', room);
    return;
  }
  console.log('Switching to chat room:', room);
  if (subscription) subscription.unsubscribe();
  selectedChat = room;
  subscription =
      stompClient.subscribe('/topic/' + selectedChat, onMessageReceived);
  stompClient.send(
      '/app/chat.addUser/' + selectedChat, {},
      JSON.stringify({sender: user.username, type: 'JOIN'}))

  clearMessages();
  showOldMessanges();
}


function clearMessages() {
  while (messageArea.firstChild) {
    messageArea.removeChild(messageArea.firstChild);
  }
}
