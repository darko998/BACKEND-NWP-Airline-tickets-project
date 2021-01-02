package rs.edu.raf.nwpproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.nwpproject.authentication.AuthService;
import rs.edu.raf.nwpproject.models.User;
import rs.edu.raf.nwpproject.models.UserType;
import rs.edu.raf.nwpproject.services.UserService;
import rs.edu.raf.nwpproject.services.UserTypeService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user/types")
public class UserTypeController {

    private final UserTypeService userTypeService;

    @Autowired
    private AuthService authService;

    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUserTypes(@RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){

            return ResponseEntity.ok(userTypeService.findAll());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }
}
