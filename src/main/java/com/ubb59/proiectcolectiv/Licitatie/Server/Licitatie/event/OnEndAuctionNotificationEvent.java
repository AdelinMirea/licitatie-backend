package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.event;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.Auction;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import org.springframework.context.ApplicationEvent;

public class OnEndAuctionNotificationEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private Auction auction;
    private User user;

    public OnEndAuctionNotificationEvent(User user, Auction auction) {
        super(user);
        this.user = user;
        this.auction = auction;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
