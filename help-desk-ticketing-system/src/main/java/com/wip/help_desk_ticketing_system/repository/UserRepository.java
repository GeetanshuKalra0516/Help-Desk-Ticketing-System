package com.wip.help_desk_ticketing_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wip.help_desk_ticketing_system.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
}
