package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.service.service.CustomUserDetailService;
import com.github.moviereservationbe.service.service.ReservationService;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    @PostMapping("/add")
    public ResponseDto ticketReservation(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ReservationRequest reservationRequest){
        return reservationService.ticketReservationResult(customUserDetails,reservationRequest);
    }



}