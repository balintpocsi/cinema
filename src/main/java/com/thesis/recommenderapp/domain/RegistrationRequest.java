package com.thesis.recommenderapp.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RegistrationRequest {

    @NotEmpty(message = "Username cannot be empty")
    private String userName;
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 4, max = 20, message = "Password must be at least 4 and maximum 20 characters long")
    private String password;

}
