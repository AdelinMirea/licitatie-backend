package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import org.springframework.stereotype.Component;

@Component
public class Validator {
    public void validateUser(User user) throws DataValidationException {
        if (user.getFirstName() == null
                || user.getLastName() == null
                || user.getLastActive() == null
                || user.getMail() == null
                || user.getNumberOfCredits() == null
                || user.getNumberOfRatings() == null
                || user.getVerified() == null
                || user.getPassword() == null
                || user.getUserToken() == null) {
            throw new DataValidationException("User must have no null fields");
        }
        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty()) {
            throw new DataValidationException("User's name must not be empty");
        }
        if (user.getPassword().length() < 6) {
            throw new DataValidationException("User's password must have at least 6 characters");
        }
    }
}
