package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.AuthenticationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.DataValidationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.TokenException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.Validator;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    private UserRepository userRepository;
    private Validator validator;

    @Autowired
    public UserService(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    public User addUser(User user) throws DataValidationException {
        if (!userRepository.findAllByMailEquals(user.getMail()).isEmpty()) {
            throw new EntityExistsException("User already exists");
        }
        validator.validateUser(user);
        String hashedPassword = DigestUtils.sha256Hex(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User getUserById(Integer id) throws DataValidationException {
        return userRepository.findById(id).orElseThrow(() -> new DataValidationException("User does not exist"));

    }

    public User updateUser(String token, String firstName, String lastName, String email) throws TokenException, DataValidationException {
        Optional<User> userOptional = userRepository.findByUserToken(token);
        if(!userOptional.isPresent()){
            throw new TokenException("Invalid token");
        }else {
            User updatedUser = userOptional.get();
            updatedUser.setFirstName(firstName);
            updatedUser.setLastName(lastName);
            updatedUser.setMail(email);
            validator.validateUser(updatedUser);
            userRepository.save(updatedUser);
            return updatedUser;
        }
    }

    public void updateUserPassword(String token, String oldPassword, String newPassword) throws TokenException, DataValidationException {
        Optional<User> userOptional = userRepository.findByUserToken(token);
        if(!userOptional.isPresent()){
            throw new TokenException("Invalid token");
        }else {
            User updatedUser = userOptional.get();
            String hashedPasswordOldPassword = DigestUtils.sha256Hex(oldPassword);
            if(!updatedUser.getPassword().equals(hashedPasswordOldPassword)){
                throw new DataValidationException("Invalid old password");
            }
            updatedUser.setPassword(newPassword);
            validator.validateUser(updatedUser);
            updatedUser.setPassword(DigestUtils.sha256Hex(newPassword));
            userRepository.save(updatedUser);
        }
    }

    public String getUserTokenByCredentials(String email, String password) throws AuthenticationException {
        String hashedPassword = DigestUtils.sha256Hex(password);
        List<User> users = userRepository.findAllByMailEquals(email);
        if (users.isEmpty()) {
            throw new AuthenticationException("No user with such email exists");
        }
        User user = users.get(0);
        if (!user.getPassword().equals(hashedPassword)) {
            throw new AuthenticationException("Wrong password");
        }
        return user.getUserToken();
    }
}
