package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
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
    {return myPageService.UserDetail(customUserDetails);}

    @PutMapping("/userInfo")
    public ResponseDto MyPageUserDetailUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody MyPageUserDetailRequest myPageUserDetailRequest)
    {return myPageService.updateUserDetail(customUserDetails,myPageUserDetailRequest);}

    @PostMapping("/review/add")
    public ResponseDto AddReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,@RequestBody ReviewRequest reviewRequest)
    {
        try {
            return myPageService.AddReview(customUserDetails,reviewRequest);
        } catch (ReviewAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/review/list")
    public ResponseDto ReviewList(@AuthenticationPrincipal CustomUserDetails customUserDetails,Pageable pageable)
    {return myPageService.findAllReviews(customUserDetails,pageable);}

    @PutMapping("/review/update")
    public ResponseDto UpdateReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,@RequestBody ReviewRequest reviewRequest)
    {return myPageService.updateReview(customUserDetails,reviewRequest);}

    @DeleteMapping("/review/delete/{reviewId}")
    public ResponseDto deleteReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("reviewId") Integer reviewId) {
        try {
            return myPageService.deleteReview(customUserDetails, reviewId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
