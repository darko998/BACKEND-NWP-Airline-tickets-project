package rs.edu.raf.nwpproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.nwpproject.authentication.AuthService;
import rs.edu.raf.nwpproject.dtos.TicketDto;
import rs.edu.raf.nwpproject.models.Reservation;
import rs.edu.raf.nwpproject.models.Ticket;
import rs.edu.raf.nwpproject.services.TicketService;
import rs.edu.raf.nwpproject.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    private AuthService authService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Ticket> getAllTickets(@RequestHeader("Authorization") String jwt) {
        return ticketService.findAll();
    }

    @GetMapping(value="/dtos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTicketDtos(@RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){

            return ResponseEntity.ok(ticketService.getTicketDtos());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){
            Optional<Ticket> optionalTicket = ticketService.findById(id);

            if(optionalTicket.isPresent()){
                return ResponseEntity.ok(optionalTicket.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<?> getUsersReservedTickets(@PathVariable("userId") Long userId, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){
            List<TicketDto> tickets = ticketService.getUserReservedTickets(userId);

            if(tickets != null) {
                return ResponseEntity.ok(tickets);
            }

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @GetMapping(value = "/company/{companyId}")
    public ResponseEntity<?> getCompanyTickets(@PathVariable("companyId") Long companyId, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){

            List<TicketDto> tickets = ticketService.getCompanyTickets(companyId);

            if(tickets != null) {
                return ResponseEntity.ok(tickets);
            }

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){

            Ticket createdTicket = ticketService.create(ticket);

            if(createdTicket != null) {
                return ResponseEntity.ok(createdTicket);
            }

            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTicket(@RequestBody Ticket ticket, @RequestHeader("Authorization") String jwt) {


        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){

            Optional<Ticket> optionalTicket = ticketService.findById(ticket.getId());

            if(optionalTicket.isPresent()) {
                Ticket updatedTicket = ticketService.update(ticket);

                return ResponseEntity.ok(updatedTicket);
            }

            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findTicketById(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){

            Optional<Ticket> optionalTicket = ticketService.findById(id);

            if(optionalTicket.isPresent()){
                return ResponseEntity.ok(optionalTicket.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){

            ticketService.deleteById(id);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }
}
