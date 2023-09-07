package com.jegan.blogapplication.restcontroller;

import com.jegan.blogapplication.dto.PostRequestDTO;
import com.jegan.blogapplication.dto.UserRequestDTO;
import com.jegan.blogapplication.entity.User;
import com.jegan.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService=userService;
    }

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody UserRequestDTO userRequestDTO) {
        User user=new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setRole("ROLE_AUTHOR");
        user.setActive(true);
        user.setPassword("{noop}"+userRequestDTO.getPassword());
        userService.save(user);
        return ResponseEntity.ok("User Created successfully");
    }
}
