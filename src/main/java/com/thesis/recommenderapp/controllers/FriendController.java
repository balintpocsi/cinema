package com.thesis.recommenderapp.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thesis.recommenderapp.domain.User;
import com.thesis.recommenderapp.service.UserService;

@Controller
public class FriendController {

    @Autowired
    private UserService userService;

    @RequestMapping("addFriend")
    public String addFriend(@RequestParam Long userId, Principal principal) {
        User currentUser = userService.getUserByUserName(principal.getName());
        userService.addFriend(userId, currentUser.getId());
        return "redirect:friends_list";
    }

    @RequestMapping("deleteFriend")
    public String deleteFriend(@RequestParam Long userId, Principal principal) {
        User currentUser = userService.getUserByUserName(principal.getName());
        userService.deleteFriend(userId, currentUser.getId());
        return "redirect:friends_list";
    }

}
