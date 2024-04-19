package com.github.moviereservationbe.service.service;

import com.github.moviereservationbe.repository.role.Role;
import com.github.moviereservationbe.repository.role.RoleJpa;
import com.github.moviereservationbe.repository.user.User;
import com.github.moviereservationbe.repository.user.UserJpa;
import com.github.moviereservationbe.repository.userRole.UserRole;
import com.github.moviereservationbe.repository.userRole.UserRoleJpa;
import com.github.moviereservationbe.web.DTO.auth.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RoleJpa roleJpa;
    private final UserRoleJpa userRoleJpa;
    private final UserJpa userJpa;

    private final PasswordEncoder passwordEncoder;


    public boolean signUp(SignUpRequestDto signUpRequestDto) {
        if(userJpa.existsByMyId(signUpRequestDto.getMyId())){
            return false;
        }

        Role role= roleJpa.findByName("ROLE_USER");

        User user= User.builder()
                .name(signUpRequestDto.getName())
                .myId(signUpRequestDto.getMyId())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .birthday(signUpRequestDto.getBirthday())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .build();
        userJpa.save(user);
        userRoleJpa.save(
                UserRole.builder()
                        .user(user)
                        .role(role)
                        .build()
        );
        return true;
    }
}
