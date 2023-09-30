package com.example.chatmultiusersocket.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chatmultiusersocket.chat.entity.MessageEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findAll();

    Optional<MessageEntity> findById(long id);

    List<MessageEntity> findByFromUserId(long fromUserId);

    List<MessageEntity> findByToGroupId(long toGroupId);

    void deleteByFromUserIdAndToGroupId(Long fromUserId, Long toGroupId);

}
