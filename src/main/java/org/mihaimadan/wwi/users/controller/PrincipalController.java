package org.mihaimadan.wwi.users.controller;

import org.mihaimadan.wwi.configuration.CustomUserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class PrincipalController {

    @GetMapping("/me")
    public CustomUserPrincipal getActiveUser(Principal principal) {
        return (CustomUserPrincipal)((UsernamePasswordAuthenticationToken)principal).getPrincipal();
    }
}
