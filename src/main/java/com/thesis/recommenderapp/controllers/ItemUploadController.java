package com.thesis.recommenderapp.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.thesis.recommenderapp.domain.SearchString;
import com.thesis.recommenderapp.domain.UploadItemRequest;
import com.thesis.recommenderapp.service.ItemService;
import com.thesis.recommenderapp.service.exceptions.SearchReturnedErrorException;
import com.thesis.recommenderapp.service.exceptions.ShouldBeMoreSpecificException;

@Controller
public class ItemUploadController {

    @Autowired
    private ItemService itemService;

    @ModelAttribute("searchString")
    public SearchString createSearchStringModel() {
        return new SearchString();
    }

    @ModelAttribute("uploadItem")
    public UploadItemRequest createUploadItemRequestModel() {
        return new UploadItemRequest();
    }

    @RequestMapping("upload")
    public String upload(Model model, @RequestParam(defaultValue = "") String substring) {
        model.addAttribute("substring", substring);
        return "upload";
    }

    @RequestMapping(value = "uploadItemPost", method = RequestMethod.POST)
    public String uploadItemPost(@ModelAttribute("uploadItem") @Valid UploadItemRequest uploadItemRequest, BindingResult bindingResult, HttpServletResponse response, Model model) {
        String result;
        if (bindingResult.hasErrors()) {
            result = "upload";
        } else {
            result = trySaveItem(uploadItemRequest, bindingResult, response, model);
        }
        return result;
    }

    @RequestMapping("confirmUpload")
    public String confirmUpload(@CookieValue("imdbId") String imdbId, HttpServletResponse response) {
        Long id = itemService.saveByImdbId(imdbId);
        deleteCookie(response);
        return "redirect:details?itemId=" + id;
    }

    private String trySaveItem(@ModelAttribute("uploadItem") @Valid UploadItemRequest uploadItemRequest, BindingResult bindingResult, HttpServletResponse response, Model model) {
        String result;
        try {
            Long id = itemService.saveItem(uploadItemRequest);
            result = "redirect:details?itemId=" + id;
        } catch (SearchReturnedErrorException e) {
            bindingResult.rejectValue("titleOrURL", "error.notFound", "I couldn't find that item on imdb.");
            result = "upload";
        } catch (ShouldBeMoreSpecificException e) {
            bindingResult.rejectValue("titleOrURL", "error.notSpecificEnough", createMessage(e));
            model.addAttribute("notSpecificEnough", true);
            response.addCookie(new Cookie("imdbId", e.getImdbId()));
            result = "upload";
        }
        return result;
    }

    private String createMessage(ShouldBeMoreSpecificException e) {
        return "Did you mean: " + e.getMessage() + "? Then click the confirm button below. Otherwise please add a more specific title.";
    }

    private void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("imdbId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
