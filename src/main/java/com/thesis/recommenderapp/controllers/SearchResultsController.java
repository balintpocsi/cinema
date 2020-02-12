package com.thesis.recommenderapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.thesis.recommenderapp.domain.Item;
import com.thesis.recommenderapp.domain.SearchString;
import com.thesis.recommenderapp.domain.User;
import com.thesis.recommenderapp.service.ItemService;
import com.thesis.recommenderapp.service.UserService;

@Controller
public class SearchResultsController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @ModelAttribute("searchString")
    public SearchString createSearchStringModel() {
        return new SearchString();
    }

    @RequestMapping(value = "searchBySubstring", method = RequestMethod.POST)
    public String searchBySubstring(Model model, SearchString searchString,
                                    @RequestParam(required = false, value = "itemSearch") String itemSearch,
                                    @RequestParam(required = false, value = "userSearch") String userSearch) {
        String result = "";
        if (itemSearch != null) {
            result = "redirect:searchItem?substring=" + searchString.getSearchSubstring();
        } else if (userSearch != null) {
            result = "redirect:searchUser?substring=" + searchString.getSearchSubstring();
        }
        return result;
    }

    @RequestMapping(value = "searchItem")
    public String searchItem(Model model,
                             @RequestParam("substring") String substring,
                             @PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable itemPageable) {
        Page<Item> itemPage = itemService.getItemsBySubstring(substring, itemPageable);
        model.addAttribute("items", itemPage.getContent());
        model.addAttribute("substring", substring);
        model.addAttribute("totalPages", itemPage.getTotalPages());
        model.addAttribute("current", itemPageable.getPageNumber());
        model.addAttribute("previous", itemPageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", itemPageable.next().getPageNumber());
        return "item_search_results";
    }

    @RequestMapping(value = "searchUser")
    public String searchUser(Model model,
                             @RequestParam("substring") String substring,
                             @PageableDefault(sort = "userName", direction = Sort.Direction.ASC) Pageable userPageable) {
        Page<User> userPage = userService.getUsersBySubstring(substring, userPageable);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("substring", substring);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("current", userPageable.getPageNumber());
        model.addAttribute("previous", userPageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", userPageable.next().getPageNumber());
        return "user_search_results";
    }

}
