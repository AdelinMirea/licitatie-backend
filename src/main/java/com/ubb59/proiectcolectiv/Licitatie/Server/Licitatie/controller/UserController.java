package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.controller;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service.UserService;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.util.List;

@RestController
@CrossOrigin(
        origins = {"*"}
)
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({"/users"})
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            String userToken = String.valueOf(System.currentTimeMillis());
            user.setUserToken(userToken);
            user = userService.addUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (DataValidationException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
