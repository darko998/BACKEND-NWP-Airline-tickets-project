package rs.edu.raf.nwpproject.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.nwpproject.models.Reservation;
import rs.edu.raf.nwpproject.repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService implements IService<Reservation, Long> {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation create(Reservation reservation) {

        Reservation existReservation = reservationRepository.findByFlightIdAndUserIdAndTicketId(reservation.getFlightId(),
                reservation.getUserId(), reservation.getTicketId());

        if(existReservation != null) {
            return null;
        }

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation update(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> findAll() {
        return (List<Reservation>) reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public Reservation findByFlightIdAndUserIdAndTicketId (int flightId, int userId, int ticketId) {
        return reservationRepository.findByFlightIdAndUserIdAndTicketId(flightId, userId, ticketId);
    }

    public void deleteByFlightIdAndUserIdAndTicketId (int flightId, int userId, int ticketId) {
        reservationRepository.deleteByFlightIdAndUserIdAndTicketId(flightId, userId, ticketId);
    }
}
