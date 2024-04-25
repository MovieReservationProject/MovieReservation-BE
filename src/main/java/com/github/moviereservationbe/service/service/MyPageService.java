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
import com.github.moviereservationbe.service.exceptions.BadRequestException;
import com.github.moviereservationbe.service.exceptions.NotFoundException;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.myPage.ReviewDto;
import com.github.moviereservationbe.web.DTO.myPage.UserUpdateRequestDto;
import com.github.moviereservationbe.web.DTO.myPage.UserUpdateResponseDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserJpa userJpa;
    private final MovieJpa movieJpa;
    private final ReservationJpa reservationJpa;
    private final ReviewJpa reviewJpa;
    private final PasswordEncoder passwordEncoder;

    public ResponseDto findMyReservation(CustomUserDetails customUserDetails) {
        User user= userJpa.findByMyIdFetchJoin(customUserDetails.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with myId: "+ customUserDetails.getMyId()));
        List<Reservation> reservationList= reservationJpa.findByUser(user);
        if(reservationList.isEmpty()) throw new NotFoundException("User has no reservation");

        //list를 받아 list를 return하는 경우
        List<ReservationResponseDto> reservationResponseDtoList= reservationList.stream()
                .map(reservation -> new ReservationResponseDto(
                        reservation.getReserveNum(),
                        reservation.getSchedule().getMovie().getTitleKorean(),
                        reservation.getSchedule().getCinemaType().getCinema().getCinemaName(),
                        reservation.getSchedule().getStartTime().toLocalDate(),
                        reservation.getSchedule().getStartTime().toLocalTime(),
                        reservation.getReserveTime()
                ) ).collect(Collectors.toList());

        return new ResponseDto(HttpStatus.OK.value(), "User reservation find success", reservationResponseDtoList);
    }

    public ResponseDto updateMyInfo(CustomUserDetails customUserDetails, UserUpdateRequestDto userUpdateRequestDto) {
        User user= userJpa.findByMyIdFetchJoin(customUserDetails.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with myId: "+ customUserDetails.getMyId()));
        user.setMyId(userUpdateRequestDto.getMyId());
        user.setPassword(passwordEncoder.encode(userUpdateRequestDto.getPassword()));
        user.setBirthday(userUpdateRequestDto.getBirthday());
        user.setPhoneNumber(userUpdateRequestDto.getPhoneNumber());
        userJpa.save(user);

        UserUpdateResponseDto userUpdateResponseDto= UserUpdateResponseDto.builder()
                .name(user.getName())
                .myId(user.getMyId())
                .birthday(user.getBirthday())
                .phoneNumber(user.getPhoneNumber())
                .build();

        return new ResponseDto(HttpStatus.OK.value(), "User info update success", userUpdateResponseDto);
    }


    public ResponseDto addReview(CustomUserDetails customUserDetails, Integer movieId, ReviewDto reviewDto) {
        User user= userJpa.findByMyIdFetchJoin(customUserDetails.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with myId: "+ customUserDetails.getMyId()));
        Movie movie= movieJpa.findById(movieId)
                .orElseThrow(()-> new NotFoundException("Cannot find movie with Id: "+ movieId));
        //only one review in one movie
        if(movieJpa.existsById(movie.getMovieId())) throw new BadRequestException("You have already posted review for this movie");

        Review review= Review.builder()
                .user(user)
                .movie(movie)
                .score(reviewDto.getScore())
                .content(reviewDto.getContent())
                .reviewDate() //date만 구하는 방법..?
                .build();

        return new ResponseDto(HttpStatus.OK.value(), "Review add success");
    }

    public ResponseDto getMyReviews(CustomUserDetails customUserDetails) {
        User user= userJpa.findByMyIdFetchJoin(customUserDetails.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with myId: "+ customUserDetails.getMyId()));
        List<Review> reviewList= reviewJpa.findByUser(user);
        if(reviewList.isEmpty()) throw new NotFoundException("User has no reviews");

        List<ReviewDto> reviewDtoList= reviewList.stream()
                .map(review -> new ReviewDto(
                        review.getScore(),
                        review.getContent()
                )).collect(Collectors.toList());

        return new ResponseDto(HttpStatus.OK.value(), "User review find success", reviewDtoList);
    }

    public ResponseDto updateMyReview(CustomUserDetails customUserDetails, Integer reviewId, ReviewDto reviewDto) {
        User user= userJpa.findByMyIdFetchJoin(customUserDetails.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with myId: "+ customUserDetails.getMyId()));
        Review review= reviewJpa.findById(reviewId)
                .orElseThrow(()-> new NotFoundException("Cannot find review with Id: "+ reviewId));
        review.setScore(reviewDto.getScore());
        review.setContent(reviewDto.getContent());
        reviewJpa.save(review);

        ReviewDto reviewResponseDto= ReviewDto.builder()
                .score(review.getScore())
                .content(review.getContent())
                .reviewDate() //date만 구하는 방법..?
                .build();
        return new ResponseDto(HttpStatus.OK.value(), "Review update success", reviewResponseDto);
    }

    public ResponseDto deleteMyReview(CustomUserDetails customUserDetails, Integer reviewId) {
        User user= userJpa.findByMyIdFetchJoin(customUserDetails.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with myId: "+ customUserDetails.getMyId()));
        Review review= reviewJpa.findById(reviewId)
                .orElseThrow(()-> new NotFoundException("Cannot find review with Id: "+ reviewId));
        reviewJpa.delete(review);
        return new ResponseDto(HttpStatus.OK.value(), "Review delete success");
    }
}
