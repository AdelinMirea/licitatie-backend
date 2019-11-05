package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.service;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.AuthenticationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.DataValidationException;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator.Validator;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

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

    public String getUserTokenByCredentials(String email, String password) throws AuthenticationException {
        String hashedPassword = DigestUtils.sha256Hex(password);
        List<User> users = userRepository.findAllByMailEquals(email);
        if(users.isEmpty()){
            throw new AuthenticationException("No user with such email exists");
        }
        User user = users.get(0);
        if(!user.getPassword().equals(hashedPassword)){
            throw new AuthenticationException("Wrong password");
        }
        return user.getUserToken();
    }
}
