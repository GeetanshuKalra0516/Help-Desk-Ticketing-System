package com.wip.helpdesk_ticketing_system.sevice;

import java.util.List;

import com.wip.helpdesk_ticketing_system.entity.Resolution;

public interface ResolutionService {


Resolution resolveTicket(Long ticketId, Long userId, String notes);

List<Resolution> getAllResolutions();


}
