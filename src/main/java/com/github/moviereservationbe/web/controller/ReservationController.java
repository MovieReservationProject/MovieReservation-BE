package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.service.service.CustomUserDetailService;
import com.github.moviereservationbe.service.service.ReservationService;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.reservationDto.ReservationRequest;
import com.github.moviereservationbe.web.DTO.reservationDto.ReservationChange;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/get")
    public ResponseDto getMovieScheduleList(){
        return reservationService.getMovieScheduleList();
    }
    @PostMapping("/add")
    public ResponseDto ticketReservation(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ReservationRequest reservationRequest){
        return reservationService.ticketReservationResult(customUserDetails,reservationRequest);
    }

    @PutMapping("/update")
    public ResponseDto changeTicket(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("reservation-id") Integer reservationId, @RequestBody ReservationChange reservationChange){
        return reservationService.changeTicketResult(customUserDetails,reservationId,reservationChange);
    }



}