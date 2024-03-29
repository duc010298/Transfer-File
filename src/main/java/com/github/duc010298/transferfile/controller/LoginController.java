package com.github.duc010298.transferfile.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/login")
public class LoginController {

    @GetMapping
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }
        return "login";
    }

}