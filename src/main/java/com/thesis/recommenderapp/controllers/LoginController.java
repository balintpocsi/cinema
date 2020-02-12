package com.thesis.recommenderapp.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thesis.recommenderapp.domain.LoginRequest;

@Controller
public class LoginController {

    @ModelAttribute("loginRequest")
    public LoginRequest createLoginRequest() {
        return new LoginRequest();
    }

    @RequestMapping("login")
    public String login(@ModelAttribute("loginRequest") LoginRequest loginRequest, BindingResult bindingResult, HttpSession httpSession) {
        Throwable authException = (Throwable) httpSession.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (authException != null) {
            bindingResult.reject(authException.getMessage(), authException.getMessage());
            httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        return "login";
    }

}
