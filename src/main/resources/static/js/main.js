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


var chatRooms = ['public'];           // Object to store chat rooms
var selectedChat = 'public';  // Default selected chat room
var stompClient = null;
var user = {username: null, status: 'ACTIVE'}

var colors = [
  '#2196F3', '#32c787', '#00BCD4', '#ff5652', '#ffc107', '#ff85af', '#FF9800',
  '#39bbb0'
];

function connect(event) {
  user.username = document.querySelector('#name').value.trim();
  var chatRoomName = document.querySelector('#chatRoomName').value.trim();
  fetch('/chatrooms')
      .then(response => response.json())
      .then(data => {
        for (var i = 0; i < data.length; i++) {
          chatRooms.push(data[i].name);
        }
        console.log(`Chat rooms:${chatrooms}`);
      })
      .catch(error => {
        console.error('Error fetching chat rooms:', error);
      });
  if (user.username && chatRoomName) {
    if (!chatRooms[chatRoomName]) {
      chatRooms[chatRoomName] = {messages: [], admin: user.username};
    }
    // <!-- <li><a href="#" data-room="general">General Chat</a></li> -->
    for(var i = 0; i < chatRooms.length; i++) {
      var li = document.createElement('li');
      li.innerHTML = `<a href="#" data-room="${chatRooms[i].name}">${chatRooms[i].name}</a>`;
      chatMenu.appendChild(li);
    }

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
  stompClient.subscribe('/topic/' + selectedChat, onMessageReceived);
  stompClient.send(
      '/app/chat.addUser/' + selectedChat, {},
      JSON.stringify({sender: user.username, type: 'JOIN'}))
  connectingElement.classList.add('hidden');
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
    if (user.status === 'ACTIVE' && message.status !== 'BLOCKED') {
      messageElement.classList.add('chat-message');

      var avatarElement = document.createElement('i');
      var avatarText = document.createTextNode(message.sender[0]);
      avatarElement.appendChild(avatarText);
      avatarElement.style['background-color'] = getAvatarColor(message.sender);

      messageElement.appendChild(avatarElement);

      var usernameElement = document.createElement('span');
      var usernameText = document.createTextNode(message.sender);
      usernameElement.appendChild(usernameText);
      if (user.username === chatRooms[selectedChat].admin) {
        var blockUser = document.createElement('d');
        var blockText = document.createTextNode('Block');
        blockUser.appendChild(blockText);
        messageElement.appendChild(blockUser);

        blockUser.addEventListener('click', (e) => {
          if (user.status === 'ACTIVE') {
            user.status = 'BLOCKED';
            blockUser.textContent = 'Unblock';
          } else {
            user.status = 'ACTIVE';
            blockUser.textContent = 'Block';
          }

          // Send a message to the server to update the user's status
          stompClient.send(
              '/app/chat.updateStatus/' + selectedChat, {}, JSON.stringify({
                sender: user.username,
                status: user.status,
                type: 'STATUS_UPDATE'
              }));
        });
      }
      messageElement.appendChild(usernameElement);
    } else {
      // Handle messages for blocked users
      messageElement.classList.add('blocked-message');
      message.content = 'This user is blocked.';
    }
  }

  var textElement = document.createElement('p');
  var messageText = document.createTextNode(message.content);
  textElement.appendChild(messageText);

  messageElement.appendChild(textElement);

  messageArea.appendChild(messageElement);
  messageArea.scrollTop = messageArea.scrollHeight;
}
4


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



function switchChatRoom(room) {
  console.log('Switching to chat room:', room);
  stompClient.unsubscribe('/topic/' + selectedChat);
  selectedChat = room;
}

// Add click event listener to dynamically created chat room links
chatMenu.addEventListener('click', function(e) {
  e.preventDefault();
  const room = e.target.getAttribute('data-room');
  switchChatRoom(room);
});

// Toggle sidebar button functionality
document.getElementById('toggleSidebar').addEventListener('click', function() {
  document.getElementById('sidebar').classList.toggle('hidden');
});