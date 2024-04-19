package com.pavan;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/user")
    public User userCreate(@Valid @RequestBody UserCreateRequest userCreateRequest){
        return userService.create(userCreateRequest.toUser());
    }
    @GetMapping("/user")
    public User getUserById(@RequestParam int id){
        return userService.getUserById(id);
    }
}
