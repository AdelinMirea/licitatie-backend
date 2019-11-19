package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.validator;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
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
                || user.getUserToken() == null
                || user.getComments() == null
                || user.getBids() == null
                || user.getRating() == null
                || user.getAuctions() == null) {
            throw new DataValidationException("User must have no null fields");
        }
        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty()) {
            throw new DataValidationException("User's name must not be empty");
        }
        if (user.getPassword().length() < 6) {
            throw new DataValidationException("User's password must have at least 6 characters");
        }
    }

    public void validateAuction(Auction auction) throws DataValidationException{
        if(auction.getId() == null
                || auction.getTitle() == null
                || auction.getDescription() == null
                || auction.getDateAdded() == null
                || auction.getClosed() == null
                || auction.getStartingPrice() == null
                || auction.getIsPrivate() == null
                || auction.getOwner() == null
                || auction.getCategory() == null
        ){
            throw new DataValidationException("The auction has null fields");
        }

        if(auction.getTitle().isEmpty()){
            throw new DataValidationException("The auction must have a title");
        }
        if(auction.getDescription().isEmpty()){
            throw new DataValidationException("The auction must have a description");
        }
    }
}
