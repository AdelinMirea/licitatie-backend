package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.event;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import org.springframework.context.ApplicationEvent;


public class OnRegistrationSuccessEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private String appUrl;
    private User user;

    public OnRegistrationSuccessEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}