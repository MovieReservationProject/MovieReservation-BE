package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.service.service.ReservationService;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationRequestDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationUpdateDto;
import com.github.moviereservationbe.web.DTO.reservation.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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

    @PutMapping("/update")
    public ResponseDto update(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @RequestBody ReservationUpdateDto reservationUpdateDto){
        return reservationService.update(customUserDetails, reservationUpdateDto);
    }

    @DeleteMapping("/delete/{reserveNum}")
    public ResponseDto delete(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @PathVariable String reserveNum){
        return reservationService.delete(customUserDetails, reserveNum);
    }

    @GetMapping("/list/findCinema/{titleKorean}")
    public List<String> findMovieCinemas(@PathVariable String titleKorean){
        return reservationService.findMovieCinemas(titleKorean);
    }
    @GetMapping("/list/findDates/{titleKorean}/{cinemaName}")
    public List<LocalDate> findMovieDates(@PathVariable String titleKorean, @PathVariable String cinemaName){
        return reservationService.findMovieDates(titleKorean, cinemaName);
    }

    @GetMapping("/list/findDates/{titleKorean}/{cinemaName}/{movieDate}")
    public List<ScheduleResponseDto> findMovieTimes(@PathVariable String titleKorean, @PathVariable String cinemaName, @PathVariable LocalDate movieDate){
        return reservationService.findMovieTimes(titleKorean, cinemaName, movieDate);
    }

}
