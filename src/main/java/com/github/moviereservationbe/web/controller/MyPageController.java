package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
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
@RequestMapping(value="mypage/")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/reservation")
    public List<MyPageReservationResponse> MyPageReservaionList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable)
    {return myPageService.findAllReservation(customUserDetails,pageable);}

    @PutMapping("userInfo")
    public MyPageUserDetailResponse MyPageUserDetailUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails, MyPageUserDetailRequest myPageUserDetailRequest)
    {return myPageService.updateUserDetail(customUserDetails,myPageUserDetailRequest);}

    @PostMapping("review/add")
    public ReviewResponse AddReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, ReviewRequest reviewRequest)
    {
        try {
            return myPageService.AddReview(customUserDetails,reviewRequest);
        } catch (ReviewAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("review/list")
    public List<ReviewResponse> ReviewList(@AuthenticationPrincipal CustomUserDetails customUserDetails,Pageable pageable)
    {return myPageService.findAllReviews(customUserDetails,pageable);}

    @PutMapping("review/update")
    public ReviewResponse UpdateReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,ReviewRequest reviewRequest)
    {return myPageService.updateReview(customUserDetails,reviewRequest);}

    @DeleteMapping("review/delete/{reviewId}")
    public ResponseDto DeleteReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Integer reviewId)
    {return myPageService.deleteReview(customUserDetails, reviewId);}
}
