package com.example.demo.Oauthhandler;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User ;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @GetMapping("/login/oauth2/code/facebook")
    public ModelAndView facebookLogin(@AuthenticationPrincipal OAuth2User  principal) {
        if (principal == null) {
            return new ModelAndView("https://www.facebook.com/signup"); 
        }

        String id = principal.getAttribute("id");
        String name = principal.getAttribute("name");
        String email = principal.getAttribute("email");

        return new ModelAndView("redirect:/welcome?name=" + name);
    }

    @GetMapping("/welcome")
    public String welcome(String name) {
        return "Welcome, " + name + "!";
    }

    @GetMapping("/login")
    public String login(String error) {

        if ("canceled".equals(error)) {

            return "login";
        }
        return "login"; 
    }
}