package com.wip.helpdesk_ticketing_system.sevice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.sevice.UserService;



@RestController
@RequestMapping("/users")
public class UserController {

@Autowired
private UserService userService;

@PostMapping
public User addUser(@RequestBody User user) {
    return userService.addUser(user);
}

@GetMapping
public List<User> getAllUsers() {
    return userService.getAllUsers();
}

@GetMapping("/{id}")
public User getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
}

@PutMapping("/{id}")
public User updateUser(@PathVariable Long id,
                       @RequestBody User user) {
    return userService.updateUser(id, user);
}

@DeleteMapping("/{id}")
public String deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return "User Deleted Successfully";
}


}
