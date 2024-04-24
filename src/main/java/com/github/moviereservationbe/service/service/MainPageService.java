package com.github.moviereservationbe.service.service;

import com.github.moviereservationbe.repository.MainPage.actor.Actor;
import com.github.moviereservationbe.repository.MainPage.actor.ActorJpa;
import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.repository.MainPage.movie.MovieJpa;
import com.github.moviereservationbe.repository.ReservationPage.cinemaType.CinemaType;
import com.github.moviereservationbe.repository.ReservationPage.cinemaType.CinemaTypeJpa;
import com.github.moviereservationbe.repository.ReservationPage.schedule.Schedule;
import com.github.moviereservationbe.repository.ReservationPage.schedule.ScheduleJpa;
import com.github.moviereservationbe.repository.review.Review;
import com.github.moviereservationbe.repository.review.ReviewJpa;
import com.github.moviereservationbe.service.exceptions.NotFoundException;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.mainPage.ActorResponseDto;
import com.github.moviereservationbe.web.DTO.mainPage.MainPageResponseDto;
import com.github.moviereservationbe.web.DTO.mainPage.MovieDetailResponseDto;
import com.github.moviereservationbe.web.DTO.mainPage.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainPageService {
    private final MovieJpa movieJpa;
    private final ScheduleJpa scheduleJpa;
    private final CinemaTypeJpa cinemaTypeJpa;
    private ActorJpa actorJpa;
    private ReviewJpa reviewJpa;

//    public ResponseDto findMainPage() {
//        List<Movie> movieList= movieJpa.findAll();
//        //ticket sales
//        //remaining seats(schedule) / total seats(cinema type) * 100
//
//        //score
//        //score(review) 다 더해서/ number of review
//        List<MainPageResponseDto> mainPageResponseDtoList= movieList.stream()
//                .map(movie -> new MainPageResponseDto(
//                        movie.getPoster(),
//                        movie.getTitleKorean(),
//                        movie.getReleaseDate(),
//                        movie.getDDay()
//                ))
//                .toList()
//    }

    public ResponseDto findMovieDetail(String titleKorean) {
        Movie movie= movieJpa.findByTitleKorean(titleKorean)
                .orElseThrow(()-> new NotFoundException("Cannot find movie with title: " + titleKorean));
        //ticket sales
        //remaining seats(schedule) / total seats(cinema type) * 100
        //1. get List<schedule> to get remaining seats
        //2. for each schedule in list, get total seats from cinema type
        //3. do the math
        List<Schedule> scheduleList= movie.getScheduleList().stream().toList();
        List<Integer> remainingSeatsList= scheduleList.stream().map(Schedule::getRemainingSeats).toList();
        int totalRemainingSeats= 0;
        for(int x: remainingSeatsList)  totalRemainingSeats +=x;
        int totalSeats= caculateTotalSeats(scheduleList);
        double ticketSales = ((double) totalRemainingSeats /totalSeats) * 100.0;


        //actorList에서 배우 이름만 가져옴
        List<Actor> actorList = actorJpa.findByMovieId(movie.getMovieId());
        List<String> actorNameList= actorList.stream().map(Actor::getActorName).collect(Collectors.toList());
        //reviewList
        List<Review> reviewList= reviewJpa.findByMovieId(movie.getMovieId());
        List<ReviewResponseDto> reviewResponseDtoList= reviewList.stream()
                .map(review -> new ReviewResponseDto(
                        review.getUser().getName(),
                        review.getScore(),
                        review.getContent(),
                        review.getReviewDate()
                ))
                .toList();


        //score
        //score(review) 다 더해서 / number of review
        int scoreSum= reviewList.stream().mapToInt(Review::getScore).sum();
        int numberOfReviews=reviewList.size();
        double scoreAvg= (double) scoreSum/numberOfReviews;

        MovieDetailResponseDto movieDetailResponseDto= MovieDetailResponseDto.builder()
                .moviePoster(movie.getPoster())
                .titleKorean(movie.getTitleKorean())
                .titleEnglish(movie.getTitleEnglish())
                .ticketSales(ticketSales)
                .releaseDate(movie.getReleaseDate())
                .scoreAvg(scoreAvg)
                .dDay(movie.getDDay())
                .ageLimit(movie.getAgeLimit())
                .screenTime(movie.getScreenTime())
                .country(movie.getCountry())
                .director(movie.getDirector())
                .status(movie.getStatus())
                .summary(movie.getSummary())
                .actorNameList(actorNameList)
                .reviewResponseDtoList(reviewResponseDtoList)
                .build();
        return new ResponseDto(HttpStatus.OK.value(), "movie detail find success", movieDetailResponseDto);
    }

    private int caculateTotalSeats(List<Schedule> scheduleList){
        //get total seats per cinema
        int totalSeats= scheduleList.stream()
                .map(schedule -> schedule.getCinemaType().getTotalSeats())
                .mapToInt(Integer::intValue)
                .sum();
        return totalSeats;

    }
}
