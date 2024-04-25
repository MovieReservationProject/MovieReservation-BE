package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.service.service.MyPageService;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.myPage.ReviewDto;
import com.github.moviereservationbe.web.DTO.myPage.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/mypage")
public class MyPageController {
    private final MyPageService myPageService;
    @GetMapping("/reservation")
    public ResponseDto findMyReservation(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return myPageService.findMyReservation(customUserDetails);
    }

    @PutMapping("/userInfo")
    public ResponseDto updateMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @RequestBody UserUpdateRequestDto userUpdateRequestDto){
        return myPageService.updateMyInfo(customUserDetails, userUpdateRequestDto);
    }

    @PostMapping("/review/add/{movieId}")
    public ResponseDto addReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                 @PathVariable Integer movieId,
                                 @RequestBody ReviewDto reviewDto){
        return myPageService.addReview(customUserDetails, movieId, reviewDto);
    }

    @GetMapping("/review/list")
    public ResponseDto getMyReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return myPageService.getMyReviews(customUserDetails);
    }

    @PutMapping("/review/update/{reviewId}")
    public ResponseDto updateMyReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @PathVariable Integer reviewId,
                                        @RequestBody ReviewDto reviewDto){
        return myPageService.updateMyReview(customUserDetails, reviewId, reviewDto);
    }

    @PutMapping("/review/delete/{reviewId}")
    public ResponseDto deleteMyReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @PathVariable Integer reviewId){
        return myPageService.deleteMyReview(customUserDetails, reviewId);
    }

}
