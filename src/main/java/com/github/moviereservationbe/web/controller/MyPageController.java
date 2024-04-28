package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.service.exceptions.NotFoundException;
import com.github.moviereservationbe.service.exceptions.ReviewAlreadyExistsException;
import com.github.moviereservationbe.service.service.MyPageService;
import com.github.moviereservationbe.web.DTO.MyPage.*;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/reservation")
    public ResponseDto MyPageReservaionList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable)
    {return myPageService.findAllReservation(customUserDetails,pageable);}

    @GetMapping("/userInfo")
    public ResponseDto MyPageUserDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {return myPageService.findUserDetail(customUserDetails);}

    @PutMapping("/userInfo")
    public ResponseDto MyPageUserDetailUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody MyPageUserDetailRequest myPageUserDetailRequest)
    {return myPageService.updateUserDetail(customUserDetails,myPageUserDetailRequest);}

    @PostMapping("/review/add/{movieId}")
    public ResponseDto AddReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("movieId") int movieId, @RequestBody ReviewRequest reviewRequest) {
        try {
            return myPageService.addReview(customUserDetails, reviewRequest, movieId);
        } catch (ReviewAlreadyExistsException e) {
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @GetMapping("/review/list")
    public ResponseDto ReviewList(@AuthenticationPrincipal CustomUserDetails customUserDetails,Pageable pageable)
    {return myPageService.findAllReviews(customUserDetails,pageable);}

    @PutMapping("/review/update/{movieId}")
    public ResponseDto UpdateReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ReviewRequest reviewRequest, @PathVariable("movieId") Integer movieId)
    {return myPageService.updateReview(customUserDetails, reviewRequest, movieId);}

    @DeleteMapping("/review/delete/{movieId}")
    public ResponseDto deleteReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("movieId") Integer movieId) {
        try {
            return myPageService.deleteReview(customUserDetails, movieId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
