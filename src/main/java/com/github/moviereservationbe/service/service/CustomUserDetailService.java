package com.github.moviereservationbe.service.service;

import com.github.moviereservationbe.repository.Auth.role.Role;
import com.github.moviereservationbe.repository.Auth.user.User;
import com.github.moviereservationbe.repository.Auth.user.UserJpa;
import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.repository.Auth.userRole.UserRole;
import com.github.moviereservationbe.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class CustomUserDetailService implements UserDetailsService {

    private final UserJpa userJpa;
    @Override
    public UserDetails loadUserByUsername(String myId) throws UsernameNotFoundException {
        User user = userJpa.findByMyIdFetchJoin(myId)
                .orElseThrow(()-> new NotFoundException("Cannot find user with ID"));

        return CustomUserDetails.builder()
                .userId(user.getUserId())
                .myId(user.getMyId())
                .password(user.getPassword())
                .authorities(user.getUserRoleList().stream().map(UserRole::getRole).map(Role::getName).collect(Collectors.toList()))
                .build();
    }
}
