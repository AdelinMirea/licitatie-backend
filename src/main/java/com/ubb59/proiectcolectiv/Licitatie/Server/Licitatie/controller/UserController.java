package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.controller;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.AuthenticationDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.UpdatePasswordDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.UserDTO;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service.UserService;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.AuthenticationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.DataValidationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.TokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.util.List;

@RestController
@CrossOrigin(
        origins = {"*"}
)
public class UserController {

    private UserService userService;
    private DTOUtils dtoUtils;

    @Autowired
    public UserController(UserService userService, DTOUtils dtoUtils) {
        this.userService = userService;
        this.dtoUtils = dtoUtils;
    }

    @PostMapping({"/users"})
    public ResponseEntity<UserDTO> addUser(@RequestBody AuthenticationDTO authenticationDTO) {
        try {
            String userToken = String.valueOf(System.currentTimeMillis());
            User user = dtoUtils.createUserFromAuthentication(authenticationDTO, userToken);
            user = userService.addUser(user);
            return new ResponseEntity<>(dtoUtils.userToUserDTO(user), HttpStatus.OK);
        } catch (DataValidationException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping({"/users"})
    public ResponseEntity<UserDTO> updateUser(@RequestHeader("token") String token, @RequestBody AuthenticationDTO authDTO) {
        try {
            User updatedUser = userService.updateUser(token, authDTO.getFirstName(), authDTO.getLastName(), authDTO.getMail());
            return new ResponseEntity<>(dtoUtils.userToUserDTO(updatedUser), HttpStatus.OK);
        } catch (TokenException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (DataValidationException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping({"/users/password"})
    public ResponseEntity<UserDTO> updateUserPassword(@RequestHeader("token") String token, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        try {
            userService.updateUserPassword(token, updatePasswordDTO.getOldPassword(), updatePasswordDTO.getNewPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TokenException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (DataValidationException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping({"/login"})
    public ResponseEntity<String> login(@RequestBody AuthenticationDTO authenticationDTO) {
        try {
            String token = userService.getUserTokenByCredentials(authenticationDTO.getMail(), authenticationDTO.getPassword());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
