package study.lock_study.redisson.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.lock_study.redisson.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {}
