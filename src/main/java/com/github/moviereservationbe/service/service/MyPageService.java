package com.github.moviereservationbe.service.service;

import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.repository.ReservationPage.reservation.ReservationJpa;
import com.github.moviereservationbe.repository.review.ReviewJpa;
import com.github.moviereservationbe.web.DTO.MyPage.*;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final ReservationJpa reservationJpa;
    private final ReviewJpa reviewJpa;

    public List<MyPageReservationResponse> findAllReservation(CustomUserDetails customUserDetails, Pageable pageable) {
   
    }

    public MyPageUserDetailResponse updateUserDetail(CustomUserDetails customUserDetails, MyPageUserDetailRequest myPageUserDetailRequest) {
    }

    public List<ReviewResponse> findAllReviews(CustomUserDetails customUserDetails, Pageable pageable) {
    }

    public ReviewResponse AddReview(CustomUserDetails customUserDetails, ReviewRequest reviewRequest) {
    }

    public ReviewResponse updateReview(CustomUserDetails customUserDetails, ReviewRequest reviewRequest) {
    }

    public ResponseDto deleteReview(CustomUserDetails customUserDetails) {
    }
}
