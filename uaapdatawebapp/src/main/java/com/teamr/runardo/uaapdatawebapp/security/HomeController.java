package com.teamr.runardo.uaapdatawebapp.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("username", principal.getAttribute("name"));  // Get the user's name from the OAuth2 user info
            model.addAttribute("email", principal.getAttribute("email"));  // Get the user's name from the OAuth2 user info
        }
        return "homepage";
    }
}