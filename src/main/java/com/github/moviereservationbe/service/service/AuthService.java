package com.github.moviereservationbe.service.service;

import com.github.moviereservationbe.config.security.JwtTokenProvider;
import com.github.moviereservationbe.repository.Auth.role.Role;
import com.github.moviereservationbe.repository.Auth.role.RoleJpa;
import com.github.moviereservationbe.repository.Auth.user.User;
import com.github.moviereservationbe.repository.Auth.user.UserJpa;
import com.github.moviereservationbe.repository.Auth.userRole.UserRole;
import com.github.moviereservationbe.repository.Auth.userRole.UserRoleJpa;
import com.github.moviereservationbe.service.exceptions.BadRequestException;
import com.github.moviereservationbe.service.exceptions.NotFoundException;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.auth.FindIdPasswordDto;
import com.github.moviereservationbe.web.DTO.auth.LoginRequestDto;
import com.github.moviereservationbe.web.DTO.auth.SignUpRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RoleJpa roleJpa;
    private final UserRoleJpa userRoleJpa;
    private final UserJpa userJpa;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    public boolean signUp(SignUpRequestDto signUpRequestDto) {
        if(userJpa.existsByMyId(signUpRequestDto.getMyId()))
            throw new BadRequestException("There is already user with ID: "+ signUpRequestDto.getMyId());

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

    public String login(LoginRequestDto loginRequestDto) {
        try{
            Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getMyId(), loginRequestDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user= userJpa.findByMyIdFetchJoin(loginRequestDto.getMyId())
                    .orElseThrow(()-> new NotFoundException("Cannot find user with ID"));
            List<String> role= user.getUserRoleList().stream().map(UserRole::getRole).map(Role::getName).collect(Collectors.toList());
            return jwtTokenProvider.createToken(loginRequestDto.getMyId(), role);
        }catch(Exception e){
            e.printStackTrace();
            throw new NotAcceptableStatusException("Login not possible");
        }
    }

    public boolean logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            String currentToken= jwtTokenProvider.resolveToken(httpServletRequest);
            jwtTokenProvider.addToBlackList(currentToken);
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
        }
        return true;
    }

    public ResponseDto findId(FindIdPasswordDto findIdPasswordDto) {
        User user= userJpa.findByNamePhoneNumber(findIdPasswordDto.getName(), findIdPasswordDto.getPhoneNumber())
                .orElseThrow(()-> new NotFoundException("Cannot find user with name and phone number"));
        return new ResponseDto(HttpStatus.OK.value(), "User ID found", user.getMyId());
    }

    public ResponseDto getNewPassword(FindIdPasswordDto findIdPasswordDto) {
        User user= userJpa.findByMyIdFetchJoin(findIdPasswordDto.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with ID"));
        String newPwd = RandomStringUtils.randomAlphanumeric(10);
        user.setPassword(passwordEncoder.encode(newPwd));
        userJpa.save(user);
        return new ResponseDto(HttpStatus.OK.value(), "New Password.  "+ newPwd  + "  Please change your password");
    }
}
