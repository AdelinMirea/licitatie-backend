package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.event;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EndAuctionNotificationListener implements ApplicationListener<OnEndAuctionNotificationEvent> {

    private final MailSender mailSender;

    @Autowired
    public EndAuctionNotificationListener(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnEndAuctionNotificationEvent event) {
        User user = event.getUser();
        Auction auction = event.getAuction();
        String recipient = user.getMail();
        String subject = auction.getTitle() + " closed";
        String message;
        if(auction.getOwner().getId().equals(user.getId())){
            message = "Your auction for "+auction.getTitle() +" has been concluded.\n" +
                    "--UbbBid";
        } else if (auction.getWinningBid() != null && auction.getWinningBid().getBidder().getId().equals(user.getId())) {
            message = "Congratulations, you are the winner of "+auction.getTitle() +".\n" +
                    "--UbbBid";
        } else {
            message = "The auction for " + auction.getTitle() + " has been concluded. Unfortunately you are not the winning bidder.\n" +
                    "--UbbBid";
        }
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipient);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
