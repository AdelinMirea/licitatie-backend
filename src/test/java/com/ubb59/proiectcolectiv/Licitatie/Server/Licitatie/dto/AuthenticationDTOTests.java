package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationDTOTests {

    private AuthenticationDTO authentication;
    @Autowired
    private DTOUtils dtoUtils;

    @Before
    public void setupAuthentication(){
        authentication = new AuthenticationDTO();
        authentication.setFirstName("Marcel");
        authentication.setLastName("Mirel");
        authentication.setMail("Marcel@mirel.com");
        authentication.setPassword("Cea mai secreta parola");
    }

    @Test
    public void testConversionFromAuthenticationToUser(){
        User createdUser = dtoUtils.createUserFromAuthentication(authentication, "0101");
        assertThat(createdUser.getId(), is(0));
        assertThat(createdUser.getFirstName(), is("Marcel"));
        assertThat(createdUser.getLastName(), is("Mirel"));
        assertThat(createdUser.getMail(), is("Marcel@mirel.com"));
        assertThat(createdUser.getPassword(), is("Cea mai secreta parola"));
        assertThat(createdUser.getUserToken(), is("0101"));
        assertThat(createdUser.getVerified(), is(false));
        assertThat(createdUser.getLastActive(), is(Date.valueOf(LocalDate.of(2000, 1, 1))));
        assertThat(createdUser.getRating(), is(0d));
        assertThat(createdUser.getNumberOfRatings(), is(0));
        assertThat(createdUser.getNumberOfCredits(), is(0d));
        assertThat(createdUser.getBids().size(), is(0));
        assertThat(createdUser.getComments().size(), is(0));
        assertThat(createdUser.getAuctions().size(), is(0));
    }

}
