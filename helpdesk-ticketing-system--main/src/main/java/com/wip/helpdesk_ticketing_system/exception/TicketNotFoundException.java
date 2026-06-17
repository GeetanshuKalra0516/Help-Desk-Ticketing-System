package com.wip.helpdesk_ticketing_system.exception;

public class TicketNotFoundException
extends RuntimeException {


public TicketNotFoundException(Long id) {

    super("Ticket Not Found With Id : "
            + id);
}


}
