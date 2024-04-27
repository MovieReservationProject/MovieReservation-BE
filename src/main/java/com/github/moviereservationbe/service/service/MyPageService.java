package com.github.moviereservationbe.service.service;

import com.github.moviereservationbe.repository.Auth.user.User;
import com.github.moviereservationbe.repository.Auth.user.UserJpa;
import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.repository.MainPage.movie.MovieJpa;
import com.github.moviereservationbe.repository.ReservationPage.reservation.Reservation;
import com.github.moviereservationbe.repository.ReservationPage.reservation.ReservationJpa;
import com.github.moviereservationbe.repository.review.Review;
import com.github.moviereservationbe.repository.review.ReviewJpa;
import com.github.moviereservationbe.service.exceptions.NotFoundException;
import com.github.moviereservationbe.service.exceptions.ReviewAlreadyExistsException;
import com.github.moviereservationbe.web.DTO.MyPage.*;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@RequiredArgsConstructor
@Service
public class MyPageService {
    private final ReservationJpa reservationJpa;
    private final MovieJpa movieJpa;
    private final ReviewJpa reviewJpa;
    private final UserJpa userJpa;
    private final PasswordEncoder passwordEncoder;

    public ResponseDto findAllReservation(CustomUserDetails customUserDetails, Pageable pageable) {
        int userId = userJpa.findById(customUserDetails.getUserId()).map(User::getUserId)
                                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));

        // 예약 정보를 페이지로 조회
        Page<Reservation> reservationPage = reservationJpa.findAllByUserId(userId,pageable);
        if(reservationPage.isEmpty()){throw new NotFoundException("예매 정보를 찾을 수 없습니다.");}

        // 예약 정보를 DTO로 변환
        Page<MyPageReservationResponse> responsePage = reservationPage.map(reservation -> MyPageReservationResponse.builder()
                .reserveId(reservation.getReserveId())
                .reserveNum(reservation.getReserveNum())
                .reserveTime(reservation.getReserveTime())
                .titleKorean(String.valueOf(reservation.getSchedule().getMovie().getTitleKorean()))
                .titleEnglish(String.valueOf(reservation.getSchedule().getMovie().getTitleEnglish()))
                .cinemaName(String.valueOf(reservation.getSchedule().getCinemaType().getCinema().getCinemaName()))
                .build());

        // 페이징된 예약 정보를 반환
        return new ResponseDto(HttpStatus.OK.value(), "", responsePage);
    }


    //유저정보 조회
    public ResponseDto UserDetail(CustomUserDetails customUserDetails) {
        User user = userJpa.findById(customUserDetails.getUserId()).orElseThrow(() -> new NotFoundException("회원가입 후 이용해 주시길 바랍니다."));
        MyPageUserDetailResponse myPageUserDetailResponse=MyPageUserDetailResponse.builder()
                .name(user.getName())
                .myId(user.getMyId())
                .birthday(user.getBirthday())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .build();
        return new ResponseDto(HttpStatus.OK.value(),"",myPageUserDetailResponse);
    }

    //유저정보 변경
    public ResponseDto updateUserDetail(CustomUserDetails customUserDetails, MyPageUserDetailRequest myPageUserDetailRequest) {
        User user = userJpa.findById(customUserDetails.getUserId()).orElseThrow(() -> new NotFoundException("회원가입 후 이용해 주시길 바랍니다."));
        String newPassword=myPageUserDetailRequest.getPassword();
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        user.setPhoneNumber(myPageUserDetailRequest.getPhoneNumber());
        userJpa.save(user);
        MyPageUserDetailResponse myPageUserDetailResponse=MyPageUserDetailResponse.builder()
                .name(user.getName())
                .myId(user.getMyId())
                .birthday(user.getBirthday())
                .phoneNumber(user.getPhoneNumber())
                .password(hashedPassword)
                .build();
        return new ResponseDto(HttpStatus.OK.value(),"user detail updated successful",myPageUserDetailResponse);
    }

    //리뷰 조회
    public ResponseDto findAllReviews(CustomUserDetails customUserDetails, Pageable pageable) {
        int userId = userJpa.findById(customUserDetails.getUserId()).map(User::getUserId)
                .orElseThrow(() -> new NotFoundException("아이디를 찾을 수 없습니다."));

        Page<Review> reviews = reviewJpa.findAllByUserId(userId,pageable);
        if(reviews.isEmpty()){throw new NotFoundException("예매 정보를 찾을 수 없습니다.");}

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(review -> ReviewResponse.builder()
                        .reviewId(review.getReviewId())
                        .titleKorean(review.getMovie().getTitleKorean())
//                        .cinemaName()
                        .score(review.getScore())
                        .content(review.getContent())
                        .reviewDate(review.getReviewDate()) // 리뷰 작성 날짜 추가
                        .build())
                .collect(Collectors.toList());

        return new ResponseDto(HttpStatus.OK.value(), "", reviewResponses);
    }

    //리뷰 작성
    public ResponseDto AddReview(CustomUserDetails customUserDetails, ReviewRequest reviewRequest,int movieId) throws ReviewAlreadyExistsException {

        if(reviewJpa.findByMovieId(movieId).isEmpty()){throw new ReviewAlreadyExistsException("이미 리뷰를 작성하였습니다.");};

        Integer userId = customUserDetails.getUserId();
        movieJpa.findById(movieId).orElseThrow(()->new NotFoundException("영화를 찾을 수 없습니다."));
        Integer score = reviewRequest.getScore();
        String content = reviewRequest.getContent();

        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("평점은 0부터 10까지 가능합니다.");  // 별점을 1부터 10까지 매길 수 있게 할 예정
        }                                                                      // 해당 에러는 나지 않을 것 같지만, 의도치 않게 1~10이 아닌 값이 들어올 가능성도 있음

        Review review = Review.builder()
                .user(User.builder().userId(userId).build())  // 사용자 ID 설정
                .movie(Movie.builder().movieId(movieId).build())  // 영화 ID 설정
                .score(score)
                .content(content)
                .reviewDate(LocalDateTime.now()) // 현재 시간으로 설정
                .build();

        Review savedReview = reviewJpa.save(review);

        ReviewResponse response = ReviewResponse.builder()
                .reviewId(savedReview.getReviewId())
                .score(score)
                .content(content)
                .reviewDate(savedReview.getReviewDate())
                .build();
        return new ResponseDto(HttpStatus.OK.value(), "리뷰가 저장되었습니다.", response);
    }

    //리뷰 수정
    public ResponseDto updateReview(CustomUserDetails customUserDetails, ReviewRequest reviewRequest) {
        Integer userId = customUserDetails.getUserId();
        Movie movie = movieJpa.findById(customUserDetails.getUserId()).orElseThrow(() -> new NotFoundException("영화를 찾을 수 없습니다."));
        Integer movieId = movie.getMovieId();
        Integer score = reviewRequest.getScore(); // 수정할 평점
        String content = reviewRequest.getContent();

        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("평점은 0부터 10까지 가능합니다.");
        }

        Optional<Review> existingReview = reviewJpa.findByUserIdAndMovieId(userId, movieId);
        if (existingReview.isEmpty()) {
            throw new NotFoundException("리뷰를 찾을 수 없습니다.");
        }

        Review review = existingReview.get();
        review.setScore(score);
        review.setContent(content);
        review.setReviewDate(LocalDateTime.now()); // 수정 시간 업데이트

        Review updateReview = reviewJpa.save(review);

        ReviewResponse response = ReviewResponse.builder()
                .reviewId(updateReview.getReviewId())
                .score(updateReview.getScore())
                .content(updateReview.getContent())
                .reviewDate(updateReview.getReviewDate().atZone(ZoneId.systemDefault()).toLocalDateTime()) // LocalDateTime 으로 변환
                .build();
        return new ResponseDto(HttpStatus.OK.value(), "리뷰가 수정되었습니다.", response);
    }


    //리뷰 삭제
    public ResponseDto deleteReview(CustomUserDetails customUserDetails, Integer reviewId) {
        Optional<Review> reviewOptional = reviewJpa.findById(reviewId);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            if (customUserDetails != null && !review.getUser().getUserId().equals(customUserDetails.getUserId())) {
                throw new IllegalArgumentException("해당 리뷰를 삭제할 권한이 없습니다.");
            }
            reviewJpa.delete(review);
            return new ResponseDto("리뷰가 삭제되었습니다.");
        } else {
            throw new NotFoundException("삭제할 리뷰를 찾을 수 없습니다.");
        }
    }
}

