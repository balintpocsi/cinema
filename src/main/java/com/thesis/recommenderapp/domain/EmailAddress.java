package com.thesis.recommenderapp.domain;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class EmailAddress {

    @Email(message = "Not a valid email address")
    private String email;

}
