package study.lock_study.pLock.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.lock_study.pLock.entitiy.Ticket;
import study.lock_study.pLock.repository.TicketRepository;



@Transactional
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public void ticketing(Long ticketId, Long quantity, Boolean lock) {
        Ticket ticket = lock
                ? ticketRepository.findByIdWithPLock(ticketId)
                : ticketRepository.findById(ticketId).orElseThrow();
        ticket.decrease(quantity);
        ticketRepository.saveAndFlush(ticket);
    }
}
