package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventScheduler {

    private UserRepository userRepository;

    @Autowired
    public EventScheduler(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 1 1/1 *") //execute every 1st of the month
    public void resetPremiumUsersInvitationCount() {
        userRepository.findAll().parallelStream().filter(User::getPremium)
                .forEach(user -> {
                    user.setNoOfPrivateAuctions(10);
                    userRepository.save(user);
                });
    }
}
