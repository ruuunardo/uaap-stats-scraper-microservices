package com.teamr.runardo.uaapdatawebapp.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    //For log-in page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login-form";
    }


    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
