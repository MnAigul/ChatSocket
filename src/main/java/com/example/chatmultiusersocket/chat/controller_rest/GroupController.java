package com.example.chatmultiusersocket.chat.controller_rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
public class GroupController {
    @GetMapping
    public String getGroup() {
        return "Group";
    }
}
