package com.wip.helpdesk_ticketing_system.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;

import java.util.Collections;

//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        User user = userRepository.findAll()
//        		.stream()
//        		.filter(u -> u.getEmail().equals(email))
//        		.findFirst()
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
////        return new org.springframework.security.core.userdetails.User(
////                user.getEmail(),
////                user.getPasswordHash(),
////                Collections.singleton(() -> "ROLE_" + user.getRole().name())
////        );
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPasswordHash(),
//                Collections.singleton(() -> "ROLE_" + user.getRole().name())
//        );
//        
//    }
//}
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                Collections.singleton(() -> "ROLE_" + user.getRole().name())
        );
    }
}