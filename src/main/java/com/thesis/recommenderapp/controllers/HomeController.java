package com.thesis.recommenderapp.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thesis.recommenderapp.domain.SearchString;
import com.thesis.recommenderapp.service.RecommendationService;

@Controller
public class HomeController {

    @Autowired
    private RecommendationService recommendationService;

    @ModelAttribute("searchString")
    public SearchString createSearchStringModel() {
        return new SearchString();
    }

    @RequestMapping(value = {"/", "index"})
    public String index(Model model, Principal principal) {
        if (isAuthenticated()) {
            model.addAttribute("recommended", recommendationService.getRecommendations(principal.getName()));
        }
        model.addAttribute("topTenItems", recommendationService.getTopTenPopularItems());
        return "index";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

}
