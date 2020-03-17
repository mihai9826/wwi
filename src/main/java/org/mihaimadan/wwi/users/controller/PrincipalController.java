package org.mihaimadan.wwi.users.controller;

import org.mihaimadan.wwi.configuration.CustomUserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
public class PrincipalController {

    @GetMapping("/")
    public void getSlash() {}

    @GetMapping("/me")
    public CustomUserPrincipal getActiveUser(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            return (CustomUserPrincipal)((UsernamePasswordAuthenticationToken)principal).getPrincipal();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated!");
        }
    }

}
