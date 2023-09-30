package com.example.chatmultiusersocket.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chatmultiusersocket.chat.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAll();

    Optional<UserEntity> findById(long id);

    Optional<UserEntity> findByUserName(String userName);

}
