package com.wip.helpdesk_ticketing_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wip.helpdesk_ticketing_system.entity.Resolution;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Long> {

}
