package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDTO {
    /**
     * DTO used to get data from the Signup form
     */

    private String firstName;
    private String lastName;
    private String password;
    private String mail;
}
