package com.thesis.recommenderapp.controllers;

import java.security.Principal;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.thesis.recommenderapp.domain.EmailAddress;
import com.thesis.recommenderapp.domain.SearchString;
import com.thesis.recommenderapp.domain.User;
import com.thesis.recommenderapp.service.EmailSenderService;
import com.thesis.recommenderapp.service.UserService;

@Controller
public class FriendsListController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService emailSenderService;

    @ModelAttribute("searchString")
    public SearchString createSearchStringModel() {
        return new SearchString();
    }

    @ModelAttribute("emailAddress")
    public EmailAddress createEmailAddressModel() {
        return new EmailAddress();
    }

    @RequestMapping("friends_list")
    public String friendsList(Model model, Principal principal,
                              @PageableDefault(sort = "userName", direction = Sort.Direction.ASC) Pageable friendPageable) {
        Page<User> friendPage = userService.getFriends(principal.getName(), friendPageable);
        model.addAttribute("friends", friendPage.getContent());
        model.addAttribute("totalPages", friendPage.getTotalPages());
        model.addAttribute("current", friendPageable.getPageNumber());
        model.addAttribute("previous", friendPageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", friendPageable.next().getPageNumber());
        return "friends_list";
    }

    @RequestMapping("sendEmail")
    public String sendEmail(@ModelAttribute("emailAddress") @Valid EmailAddress emailAddress, BindingResult bindingResult,
                            Principal principal, Model model, HttpServletRequest request) {
        if (!bindingResult.hasErrors()) {
            sendInvite(emailAddress, principal, model, request);
        } else {
            model.addAttribute("emailError", bindingResult.getFieldError("email").getDefaultMessage());
        }
        return "forward:friends_list";
    }

    private void sendInvite(@ModelAttribute("emailAddress") EmailAddress emailAddress, Principal principal, Model model, HttpServletRequest request) {
        model.addAttribute("emailSent", true);
        User user = userService.getUserByUserName(principal.getName());
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        emailSenderService.sendInvite(emailAddress.getEmail(), user, baseUrl);
    }

}
