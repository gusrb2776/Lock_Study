package study.lock_study.redisson.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.lock_study.redisson.annotation.RedissonLock;
import study.lock_study.redisson.entity.Ticket;
import study.lock_study.redisson.repository.TicketRepository;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    /**
     * 그냥 티켓팅 처리 (티켓 갯수 줄이기)
     */
    public void ticketing(Long ticketId, Long quantity){
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.decrease(quantity);
        ticketRepository.saveAndFlush(ticket);
    }

    /**
     * 레디스를 이용한 티켓팅
     */
    @RedissonLock(value = "#ticketId")
    public void redissonTicketing(Long ticketId, Long quantity){
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.decrease(quantity);
        ticketRepository.saveAndFlush(ticket);
    }
}
