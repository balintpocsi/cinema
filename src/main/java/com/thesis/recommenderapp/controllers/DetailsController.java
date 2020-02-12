package com.thesis.recommenderapp.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thesis.recommenderapp.domain.AddToWatchListItem;
import com.thesis.recommenderapp.domain.Item;
import com.thesis.recommenderapp.domain.SearchString;
import com.thesis.recommenderapp.domain.User;
import com.thesis.recommenderapp.domain.Watched;
import com.thesis.recommenderapp.service.ItemService;
import com.thesis.recommenderapp.service.UserService;
import com.thesis.recommenderapp.service.WatchedService;

@Controller
public class DetailsController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private WatchedService watchedService;

    @ModelAttribute("addToWatchListItem")
    public AddToWatchListItem createWatchListItemModel() {
        return new AddToWatchListItem();
    }

    @ModelAttribute("searchString")
    public SearchString createSearchStringModel() {
        return new SearchString();
    }

    @RequestMapping("details")
    public String details(Model model, @RequestParam Long itemId, Principal principal,
                          @ModelAttribute("addToWatchListItem") AddToWatchListItem addToWatchListItem) {
        User user = userService.getUserByUserName(principal.getName());
        Item item = itemService.getItem(itemId);
        Watched watched = watchedService.getWatched(user, item);
        if (watched != null) {
            addToWatchListItem.setRating(watched.getRating());
        }
        item.setYear(item.getYear().replace("–", "-"));
        model.addAttribute("item", item);
        return "details";
    }

}
