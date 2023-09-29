package com.example.chatmultiusersocket.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chatmultiusersocket.chat.entity.GroupEntity;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    List<GroupEntity> findAll();
    GroupEntity findById(long id);
    GroupEntity findByName(String name);
    
    
    
}
