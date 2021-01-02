package rs.edu.raf.nwpproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.nwpproject.authentication.AuthService;
import rs.edu.raf.nwpproject.models.Company;
import rs.edu.raf.nwpproject.services.CompanyService;
import rs.edu.raf.nwpproject.services.TicketService;
import rs.edu.raf.nwpproject.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    private AuthService authService;

    @Autowired
    private final TicketService ticketService;

    public CompanyController(CompanyService companyService, TicketService ticketService) {
        this.companyService = companyService;
        this.ticketService = ticketService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCompanies(@RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){

            return ResponseEntity.ok(companyService.findAll());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCompanyById(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){
            Optional<Company> optionalCompany = companyService.findById(id);

            if(optionalCompany.isPresent()){
                return ResponseEntity.ok(optionalCompany.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCompany (@RequestBody Company company, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){
            Company existCompany = companyService.findByName(company.getName());

            if(existCompany == null) {
                Company createdCompany =  companyService.create(company);

                if(createdCompany != null) {
                    return ResponseEntity.ok(createdCompany);
                }
            }

            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCompany(@RequestBody Company company, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){
            Optional<Company> optionalCompany = companyService.findById(company.getId());

            if(optionalCompany.isPresent()) {
                Company updatedCompany =  companyService.update(company);

                return ResponseEntity.ok(updatedCompany);
            }

            return ResponseEntity.badRequest().build();

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){
            companyService.deleteById(id);

            ticketService.deleteCompanyTickets(id);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }
}
