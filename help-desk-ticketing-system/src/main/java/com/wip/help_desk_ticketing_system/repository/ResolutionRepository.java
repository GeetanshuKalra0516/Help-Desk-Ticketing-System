package com.wip.help_desk_ticketing_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wip.help_desk_ticketing_system.entity.Resolution;

public interface ResolutionRepository extends JpaRepository<Resolution, Long> {
	
}