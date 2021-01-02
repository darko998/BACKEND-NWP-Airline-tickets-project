package rs.edu.raf.nwpproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.nwpproject.authentication.AuthService;
import rs.edu.raf.nwpproject.dtos.UserDto;
import rs.edu.raf.nwpproject.models.User;
import rs.edu.raf.nwpproject.models.UserType;
import rs.edu.raf.nwpproject.services.UserService;
import rs.edu.raf.nwpproject.services.UserTypeService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserTypeService userTypeService;

    @Autowired
    private AuthService authService;

    public UserController(UserService userService, UserTypeService userTypeService) {
        this.userService = userService;
        this.userTypeService = userTypeService;
    }

    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

        User user = userService.findByUsername(username);

        if(user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setPassword(user.getPassword());
            userDto.setUserType(user.getUserType());
            userDto.setJwt(authService.generateJWT(user));

            Optional<UserType> optionalUserType = userTypeService.findById(Long.valueOf(user.getUserType()));

            if(optionalUserType.isPresent()){
                userDto.setUserTypeName(optionalUserType.get().getName());
            } else {
                return ResponseEntity.status(401).build();
            }

            return ResponseEntity.ok(userDto);
        }

        return ResponseEntity.status(401).build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create (@RequestBody User user, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){

            if(user != null && user.getId() != null) {
                Optional<User> optionalUser = userService.findById(user.getId());

                if(optionalUser.isPresent()) {
                    return ResponseEntity.badRequest().build();
                }
            }

            if(userService.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User with this username already exists!");
            }

            if(!isValidUser(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passed parameter is not valid!");
            }

            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

            User createdUser = userService.create(user);

            return ResponseEntity.ok(createdUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) ){

            return ResponseEntity.ok(userService.findAll());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt)){
            Optional<User> optionalUser = userService.findById(id);

            if(optionalUser.isPresent()){
                return ResponseEntity.ok(optionalUser.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody User user, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){

            return ResponseEntity.ok(userService.update(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        if(authService.isAuthorized(jwt) && authService.isAdmin(jwt)){

            userService.deleteById(id);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }

    public boolean isValidUser(User user) {
        if(user.getUsername() == null || user.getPassword() == null) {
            return false;
        }

        return true;
    }
}
