package com.example.chatmultiusersocket.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.chatmultiusersocket.chat.entity.GroupEntity;
import com.example.chatmultiusersocket.chat.repository.GroupRepository;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<GroupEntity> findAll() {
        return groupRepository.findAll();
    }

    public GroupEntity findById(long id) {
        return groupRepository.findById(id).orElse(null);
    }

    public GroupEntity findByName(String name) {
        return groupRepository.findByName(name).orElse(null);
    }

    public void save(GroupEntity groupEntity) {
        groupRepository.save(groupEntity);
    }

    public void delete(GroupEntity groupEntity) {
        groupRepository.delete(groupEntity);
    }

    public void deleteById(long id) {
        groupRepository.deleteById(id);
    }

    public void deleteAll() {
        groupRepository.deleteAll();
    }

}
