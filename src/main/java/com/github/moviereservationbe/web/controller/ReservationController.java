package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.service.service.ReservationService;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/add")
    public ResponseDto register(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @RequestBody ReservationRequestDto reservationRequestDto){
        return reservationService.register(customUserDetails, reservationRequestDto);
    }

}
