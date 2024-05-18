package com.github.moviereservationbe.repository.Auth.userDetails;

import com.github.moviereservationbe.repository.Auth.user.SocialUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final SocialUser socialUser;

    public UserDetailsImpl(SocialUser socialUser) {
        this.socialUser = socialUser;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return socialUser.getAuthorities;
    }

    @Override
    public String getPassword() {
        return socialUser.getPassword();
    }

    @Override
    public String getUsername() {
        return socialUser.getNickname();
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
