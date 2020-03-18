package org.mihaimadan.wwi.configuration;

import org.mihaimadan.wwi.users.model.User;
import org.mihaimadan.wwi.users.model.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserPrincipal implements UserDetails {

    private UserDTO theUser;

    public CustomUserPrincipal(User theUser) {
        this.theUser = new UserDTO();
        BeanUtils.copyProperties(theUser, this.theUser);
    }

    public UserDTO getTheUser() {
        return theUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(theUser.getRole()));
    }

    @Override
    public String getPassword() {
        return this.theUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.theUser.getFullName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
