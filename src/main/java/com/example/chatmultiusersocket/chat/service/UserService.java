package com.example.chatmultiusersocket.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.chatmultiusersocket.chat.entity.UserEntity;
import com.example.chatmultiusersocket.chat.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public void delete(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

}
