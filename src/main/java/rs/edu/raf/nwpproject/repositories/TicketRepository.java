package rs.edu.raf.nwpproject.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.nwpproject.models.Ticket;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

}
