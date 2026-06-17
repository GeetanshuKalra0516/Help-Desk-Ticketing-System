package com.wip.helpdesk_ticketing_system.sevice;

import java.util.List;

import com.wip.helpdesk_ticketing_system.entity.User;


public interface UserService {

    User addUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}