package rs.edu.raf.nwpproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.nwpproject.authentication.AuthService;
import rs.edu.raf.nwpproject.models.Flight;
import rs.edu.raf.nwpproject.models.Reservation;
import rs.edu.raf.nwpproject.models.Ticket;
import rs.edu.raf.nwpproject.services.ReservationService;
import rs.edu.raf.nwpproject.services.TicketService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    private AuthService authService;

    @Autowired
    private TicketService ticketService;

    public ReservationController(ReservationService reservationService, TicketService ticketService) {
        this.reservationService = reservationService;
        this.ticketService = ticketService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllReservations(@RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){

            return ResponseEntity.ok(reservationService.findAll());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){
            Optional<Reservation> optionalReservation = reservationService.findById(id);

            if(optionalReservation.isPresent()){
                return ResponseEntity.ok(optionalReservation.get());
            } else {
                return ResponseEntity.notFound().build();
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation,
                                               @RequestHeader("Authorization") String jwt,
                                               @RequestParam("availableNumberOfTickets") int availableNumberOfTickets) {

        if(authService.isAuthorized(jwt)){

            Optional<Ticket> ticket = ticketService.findById((long) reservation.getTicketId());
            if(ticket.isPresent()){
                if(reservation.getNumberOfTickets() > ticket.get().getCount()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no enough tickets!");
                }
            }

            Reservation createdReservation = reservationService.create(reservation);

            if(createdReservation != null) {

                int nowAvailableNumberOfTickets = availableNumberOfTickets - createdReservation.getNumberOfTickets();

                ticketService.reserveTickets(nowAvailableNumberOfTickets, createdReservation.getTicketId());

                return ResponseEntity.ok(createdReservation);
            }

            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateReservation(@RequestBody Reservation reservation, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){
            Optional<Reservation> optionalReservation = reservationService.findById(reservation.getId());

            if(optionalReservation.isPresent()) {
                Reservation updatedReservation = reservationService.update(reservation);

                return ResponseEntity.ok(updatedReservation);
            }

            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findReservationById(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){

            Optional<Reservation> optionalReservation = reservationService.findById(id);

            if(optionalReservation.isPresent()){
                return ResponseEntity.ok(optionalReservation.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable("id") Long id,
                                               @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){
            reservationService.deleteById(id);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @Transactional
    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteReservationByReservation(@RequestBody Reservation reservation,
                                                            @RequestHeader("Authorization") String jwt,
                                                            @RequestParam("availableNumberOfTickets") int availableNumberOfTickets) {

        if(authService.isAuthorized(jwt)){

            Reservation reservationForDelete =
                    reservationService.findByFlightIdAndUserIdAndTicketId(reservation.getFlightId(),
                                                                            reservation.getUserId(),
                                                                            reservation.getTicketId());

            int nowAvailableNumberOfTickets = availableNumberOfTickets + reservationForDelete.getNumberOfTickets();
            ticketService.cancelReservedTickets(nowAvailableNumberOfTickets, reservation.getTicketId());

            reservationService.deleteByFlightIdAndUserIdAndTicketId(reservation.getFlightId(),
                    reservation.getUserId(),
                    reservation.getTicketId());

            return ResponseEntity.ok(reservation);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }
}
