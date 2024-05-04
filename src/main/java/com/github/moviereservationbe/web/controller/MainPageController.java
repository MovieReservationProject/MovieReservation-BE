package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.service.service.MainPageService;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/main")
public class MainPageController {
    private final MainPageService mainPageService;
    @GetMapping("/findAll")
    public ResponseDto findMainPage(){
        return mainPageService.findMainPage();
    }

    @GetMapping("/findBySort/{sort}")
    public ResponseDto findMainPageSort(Pageable pageable,
                                        @PathVariable String sort){
        return mainPageService.findMainPageSort(pageable, sort);
    }

    @GetMapping("/findMovie/{titleKorean}")
    public ResponseDto findMovieDetail(@PathVariable String titleKorean){
        return mainPageService.findMovieDetail(titleKorean);
    }

}
