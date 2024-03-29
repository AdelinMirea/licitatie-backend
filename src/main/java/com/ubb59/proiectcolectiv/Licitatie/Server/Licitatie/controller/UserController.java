package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.controller;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.VerificationToken;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto.*;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.event.OnRegistrationSuccessEvent;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.event.OnRegistrationSuccessEvent;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service.UserService;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.AuthenticationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.DataValidationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.SameUserException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.TokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Calendar;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(
        origins = {"*"}
)
public class UserController {
    private UserService userService;
    private DTOUtils dtoUtils;
    private ApplicationEventPublisher eventPublisher;

    @Value("${appUrl}")
    private String appUrl;

    @Autowired
    public UserController(UserService userService, DTOUtils dtoUtils, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.dtoUtils = dtoUtils;
        this.eventPublisher = eventPublisher;
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
    @DeleteMapping({"/users/{id}"})
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {

            userService.deleteUser(id);
            return new ResponseEntity<>("User has been deleted", HttpStatus.OK);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (TokenException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("User not found",HttpStatus.OK);
    }

    @GetMapping({"/users/{id}"})
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(dtoUtils.userToUserDTO(user), HttpStatus.OK);
        } catch (DataValidationException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping({"/users/token/{token}"})
    public ResponseEntity<UserDTO> getUserByToken(@PathVariable String token) {
        try {
            User user = userService.getUserByToken(token);
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

    @PutMapping({"/users/{id}/rating"})
    public ResponseEntity<UserDTO> updateUserRating(@PathVariable Integer id, @RequestHeader("token") String token, @RequestBody Double rating) {
        try {
            User user = userService.getUserById(id);
            userService.updateUserRating(token, user, rating);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TokenException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (DataValidationException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (SameUserException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping({"/users/{id}/credits"})
    public ResponseEntity<UserDTO> updateUserCredits(@PathVariable Integer id, @RequestHeader("token") String token, @RequestBody Double credits){
        try {
            User user = userService.getUserById(id);
            userService.updateUserCredit(token, user, credits);
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

    @GetMapping({"/users/{id}/auctions"})
    public ResponseEntity<List<AuctionDTO>> getAuctionsCreatedByUser(@PathVariable Integer id) {
        try {
            List<AuctionDTO> auctions = userService.getAuctionsCreatedByUser(id)
                    .parallelStream().map(dtoUtils::auctionToAuctionDTO).collect(Collectors.toList());
            return new ResponseEntity<>(auctions, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({"/users/{id}/auctions/participated"})
    public ResponseEntity<List<AuctionDTO>> getAuctionsWithUserParticipated(@PathVariable Integer id) {
        try {
            List<AuctionDTO> auctions = userService.getAuctionsUserParticipated(id)
                    .parallelStream().map(dtoUtils::auctionToAuctionDTO).collect(Collectors.toList());
            return new ResponseEntity<>(auctions, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({"/users/{id}/comments"})
    public ResponseEntity<List<CommentDTO>> getCommentsCreatedByUserPaginated(@PathVariable Integer id,@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
                                                                              @RequestParam(name = "itemNumber", defaultValue = "10", required = false) Integer itemNumber) {
        try {
            List<CommentDTO> comments = userService.getCommentsCreatedByUser(id,page,itemNumber)
                    .parallelStream().map(dtoUtils::commentToCommentDTO).collect(Collectors.toList());
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registerNewUser(@RequestBody AuthenticationDTO authenticationDTO) {
        try {
            String userToken = String.valueOf(System.currentTimeMillis());
            User user = dtoUtils.createUserFromAuthentication(authenticationDTO, userToken);
            user = userService.addUser(user);
            eventPublisher.publishEvent(new OnRegistrationSuccessEvent(user, appUrl));
            return new ResponseEntity<>(user.getMail(), HttpStatus.OK);
        } catch (DataValidationException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>("User Already Exists",HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/confirmRegistration/{token}")
    public ResponseEntity<String> confirmRegistration(@PathVariable String token) {

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if(verificationToken == null) {
            return new ResponseEntity<>("Invalid link", HttpStatus.BAD_REQUEST);
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpiryDate().getTime()-calendar.getTime().getTime())<=0) {
            return new ResponseEntity<>("Link expired, try to register again", HttpStatus.NOT_ACCEPTABLE);
        }
        userService.enableRegisteredUser(user);
        return new ResponseEntity<>("User enabled", HttpStatus.OK);
    }

}
