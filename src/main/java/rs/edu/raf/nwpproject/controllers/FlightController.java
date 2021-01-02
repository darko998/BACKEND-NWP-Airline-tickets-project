package rs.edu.raf.nwpproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.nwpproject.authentication.AuthService;
import rs.edu.raf.nwpproject.dtos.FlightDto;
import rs.edu.raf.nwpproject.models.City;
import rs.edu.raf.nwpproject.models.Flight;
import rs.edu.raf.nwpproject.models.User;
import rs.edu.raf.nwpproject.services.CityService;
import rs.edu.raf.nwpproject.services.FlightService;
import rs.edu.raf.nwpproject.services.UserService;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CityService cityService;

    public FlightController(FlightService flightService, CityService cityService) {
        this.flightService = flightService;
        this.cityService = cityService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllFlights(@RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){
            return ResponseEntity.ok(flightService.findAll());

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @GetMapping(value="/dtos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllFlightDtos(@RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){
            return ResponseEntity.ok(flightService.findAllDtos());

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFlight(@RequestBody FlightDto flightDto, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){
            if(flightDto.getOriginCityId() != 0 && flightDto.getDestinationCityId() != 0) {

                Flight flightForCreate = new Flight();
                flightForCreate.setOriginCityId(flightDto.getOriginCityId());
                flightForCreate.setDestinationCityId(flightDto.getDestinationCityId());

                Flight createdFlight = flightService.create(flightForCreate);

                if (createdFlight != null) {
                    return ResponseEntity.ok(createdFlight);
                }
            } else {
                String originCityName = flightDto.getOriginCityName();
                String destinationCityName = flightDto.getDestinationCityName();

                if(originCityName.toLowerCase().equals(destinationCityName.toLowerCase())){
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Flight origin city and destination city can't have same names!");
                }

                City originCity = cityService.findByName(originCityName);
                City destinationCity = cityService.findByName(destinationCityName);

                if(originCity == null) {
                    City city = new City();
                    city.setName(originCityName);
                    originCity = cityService.create(city);
                }

                if(destinationCity == null) {
                    City city = new City();
                    city.setName(destinationCityName);
                    destinationCity = cityService.create(city);
                }

                if(flightService.findByOriginCityIdAndDestinationCityId(originCity.getId().intValue(), destinationCity.getId().intValue()) != null) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Flight with this cities already exists");
                }

                Flight flightForCreate = new Flight();
                flightForCreate.setOriginCityId(originCity.getId().intValue());
                flightForCreate.setDestinationCityId(destinationCity.getId().intValue());

                Flight createdFlight = flightService.create(flightForCreate);

                if (createdFlight != null) {
                    return ResponseEntity.ok(createdFlight);
                }
            }

            return ResponseEntity.badRequest().build();
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFlight(@RequestBody Flight flight, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){
            Optional<Flight> optionalFlight = flightService.findById(flight.getId());

            if(optionalFlight.isPresent()) {
                Flight updatedFlight = flightService.update(flight);

                return ResponseEntity.ok(updatedFlight);
            }

            return ResponseEntity.badRequest().build();
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findFlightById(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {
        if(authService.isAuthorized(jwt)){
            Optional<Flight> optionalFlight = flightService.findById(id);

            if(optionalFlight.isPresent()){
                return ResponseEntity.ok(optionalFlight.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){
            flightService.deleteById(id);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }
}
