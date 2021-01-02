package rs.edu.raf.nwpproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.nwpproject.authentication.AuthService;
import rs.edu.raf.nwpproject.models.City;
import rs.edu.raf.nwpproject.services.CityService;
import rs.edu.raf.nwpproject.services.UserService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    private AuthService authService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCities(@RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){
            return ResponseEntity.ok(cityService.findAll());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
    }
}
