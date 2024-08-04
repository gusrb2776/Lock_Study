package study.lock_study.oLock.service;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import study.lock_study.oLock.entity.Ticket;
import study.lock_study.oLock.repository.TicketRepository;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public void optimisticTicketing(Long ticketId, Long quantity) {
        try {
            Ticket ticket = ticketRepository.findByIdWithOLock(ticketId);
            ticket.decrease(quantity);
            ticketRepository.saveAndFlush(ticket);
        } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
            log.info("Version 충돌. 롤백 또는 재시도");
        }
    }
}
