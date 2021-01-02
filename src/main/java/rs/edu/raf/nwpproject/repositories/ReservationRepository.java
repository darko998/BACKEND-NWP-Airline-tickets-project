package rs.edu.raf.nwpproject.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.nwpproject.models.Reservation;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    Reservation findByFlightIdAndUserIdAndTicketId(int flightId, int userId, int ticketId);

    void deleteByFlightIdAndUserIdAndTicketId(int flightId, int userId, int ticketId);
}
