package com.example.chatmultiusersocket.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.chatmultiusersocket.chat.entity.MessageEntity;
import com.example.chatmultiusersocket.chat.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageEntity> findAll() {
        return messageRepository.findAll();
    }

    public MessageEntity findById(long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public List<MessageEntity> findByFromUserId(long fromUserId) {
        return messageRepository.findByFromUserId(fromUserId);
    }

    public List<MessageEntity> findByToGroupId(long toGroupId) {
        return messageRepository.findByToGroupId(toGroupId);
    }

    public void save(MessageEntity messageEntity) {
        messageRepository.save(messageEntity);
    }

    public void delete(MessageEntity messageEntity) {
        messageRepository.delete(messageEntity);
    }

    public void deleteById(long id) {
        messageRepository.deleteById(id);
    }

    public void deleteAll() {
        messageRepository.deleteAll();
    }

    public void deleteByFromUserIdAndToGroupId(Long fromUserId, Long toGroupId) {
        messageRepository.deleteByFromUserIdAndToGroupId(fromUserId, toGroupId);
    }

}
