package com.thesis.recommenderapp.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.thesis.recommenderapp.domain.AddToWatchListItem;
import com.thesis.recommenderapp.domain.User;
import com.thesis.recommenderapp.service.UserService;
import com.thesis.recommenderapp.service.WatchedService;

@Controller
public class EditWatchListController {

    @Autowired
    private WatchedService watchedService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "addToWatchList", method = RequestMethod.POST)
    public String addToWatchList(AddToWatchListItem addToWatchListItem, Principal principal) {
        watchedService.saveWatched(addToWatchListItem, principal.getName());
        return "redirect:watch_list";
    }

    @RequestMapping("deleteWatched")
    public String deleteItemFromWatchList(@RequestParam Long watchedId, @RequestParam Integer page, Principal principal) {
        User user = userService.getUserByUserName(principal.getName());
        watchedService.deleteWatched(user, watchedId);
        return "redirect:watch_list?page=" + page;
    }

}
