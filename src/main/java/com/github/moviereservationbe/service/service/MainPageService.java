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
import com.github.moviereservationbe.service.exceptions.BadRequestException;
import com.github.moviereservationbe.service.exceptions.NotFoundException;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.mainPage.ActorResponseDto;
import com.github.moviereservationbe.web.DTO.mainPage.MainPageResponseDto;
import com.github.moviereservationbe.web.DTO.mainPage.MovieDetailResponseDto;
import com.github.moviereservationbe.web.DTO.mainPage.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainPageService {
    private final MovieJpa movieJpa;
    private final ActorJpa actorJpa;
    private final ReviewJpa reviewJpa;

    public ResponseDto findMainPage() {
        List<Movie> movieList= movieJpa.findAll();
        //from list entity get list entity
        //하나의 영화에 대해 여러개의 스케쥴을 받고, 그 스케쥴들에서 ticket sales구해야 함
        for(Movie movie: movieList){
            //ticket sales
            List<Schedule> scheduleList= movie.getScheduleList();
            double ticketSales= caculateTicketSales(scheduleList);
            //score
            List<Review> reviewList= reviewJpa.findByMovieId(movie.getMovieId());
            double score= caculateScore(reviewList);
            //dDay
            Date releaseDate= movie.getReleaseDate();
            LocalDate localReleaseDate= releaseDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if(localReleaseDate.isBefore(LocalDate.now())) movie.setDDay(0);
            //save to movie
            movie.setTicketSales(ticketSales);
            movie.setScoreAvg(score);
            movieJpa.save(movie);
        }


        List<MainPageResponseDto> mainPageResponseDtoList= movieList.stream()
                .map(movie -> new MainPageResponseDto(
                        movie.getPoster(),
                        movie.getTitleKorean(),
                        movie.getTicketSales(),
                        movie.getReleaseDate(),
                        movie.getScoreAvg(),
                        movie.getDDay()
                ))
                .toList();
        return new ResponseDto(HttpStatus.OK.value(), "Main page find success",mainPageResponseDtoList);
    }

    public ResponseDto findMainPageSort(Pageable pageable, String sort) {
        ArrayList<String> sortArray= new ArrayList();
        sortArray.add("ticket-sales");
        sortArray.add( "reviews");

        Page<Movie> moviePage= movieJpa.findAll(pageable);
        for(Movie movie: moviePage){
            //ticketSales
            List<Schedule> scheduleList= movie.getScheduleList();
            int remainingSeats= scheduleList.stream().mapToInt(Schedule::getRemainingSeats).sum();
            int totalSeats= scheduleList.stream().map(Schedule::getCinemaType).map(CinemaType::getTotalSeats).mapToInt(Integer::intValue).sum();
            if (totalSeats == 0) throw new NotFoundException("There are no total seats for this movie");
            double ticketSales= ((double) (totalSeats- remainingSeats)/totalSeats)*100;
            double formattedTicketSales= (double) Math.round(ticketSales*10.0)/10.0;


            //scoreAvg
            List<Review> reviewList= reviewJpa.findByMovieId(movie.getMovieId());
            int scoreSum= reviewList.stream().map(Review::getScore).mapToInt(Integer::intValue).sum();
            int countReview= reviewList.size();
            if(countReview == 0 )throw new NotFoundException("There are no reviews for this movie");
            double scoreAvg= (double) scoreSum / countReview;

            movie.setTicketSales(formattedTicketSales);
            movie.setScoreAvg(scoreAvg);
            movieJpa.save(movie);
        }
        //sort
        List<MainPageResponseDto> mainPageResponseDtoList = new ArrayList<>();
        if(sort.equals("ticket-sales")){
            mainPageResponseDtoList= moviePage.stream()
                    .sorted(Comparator.comparingDouble(Movie::getTicketSales).reversed())
                    .map(movie -> new MainPageResponseDto(
                            movie.getPoster(),
                            movie.getTitleKorean(),
                            movie.getTicketSales(),
                            movie.getReleaseDate(),
                            movie.getScoreAvg(),
                            movie.getDDay()
                    ))
                    .toList();
        }else if(sort.equals("reviews")){
            mainPageResponseDtoList= moviePage.stream()
                    .sorted(Comparator.comparingDouble(Movie::getScoreAvg).reversed())
                    .map(movie -> new MainPageResponseDto(
                            movie.getPoster(),
                            movie.getTitleKorean(),
                            movie.getTicketSales(),
                            movie.getReleaseDate(),
                            movie.getScoreAvg(),
                            movie.getDDay()
                    ))
                    .toList();
        }else{
            throw new BadRequestException("Invalid sort criteria: " + sort);
        }
        return new ResponseDto(HttpStatus.OK.value(), "Main page sort find success",mainPageResponseDtoList);

    }

    public ResponseDto findMovieDetail(String titleKorean) {
        Movie movie= movieJpa.findByTitleKorean(titleKorean)
                .orElseThrow(()-> new NotFoundException("Cannot find movie with title: " + titleKorean));

        //ticket sales
        List<Schedule> scheduleList= movie.getScheduleList().stream().toList();
        double ticketSales= caculateTicketSales(scheduleList);


        //actorList에서 배우 이름만 가져옴
        List<Actor> actorList = actorJpa.findByMovieId(movie.getMovieId());
        List<String> actorNameList= actorList.stream().map(Actor::getActorName).collect(Collectors.toList());
        //reviewList
        List<Review> reviewList= reviewJpa.findByMovieId(movie.getMovieId());
        //make DTO list from Entity List
        List<ReviewResponseDto> reviewResponseDtoList= reviewList.stream()
                .map(review -> new ReviewResponseDto(
                        review.getUser().getName(),
                        review.getScore(),
                        review.getContent(),
                        review.getReviewDate()
                ))
                .toList();


        //score
        double scoreAvg= caculateScore(reviewList);

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
                .genre(movie.getGenre())
                .status(movie.getStatus())
                .summary(movie.getSummary())
                .actorNameList(actorNameList)
                .reviewResponseDtoList(reviewResponseDtoList)
                .build();
        return new ResponseDto(HttpStatus.OK.value(), "movie detail find success", movieDetailResponseDto);
    }

    private double caculateTicketSales(List<Schedule> scheduleList){
        //remaining seats(schedule) / total seats(cinema type) * 100
        //1. get List<schedule> to get remaining seats
        //2. for each schedule in list, get total seats from cinema type
        //3. do the math
        List<Integer> remainingSeatsList= scheduleList.stream().map(Schedule::getRemainingSeats).toList();
        int totalRemainingSeats= 0;
        for(int x: remainingSeatsList)  totalRemainingSeats +=x;
        int totalSeats= caculateTotalSeats(scheduleList);
        double ticketSales = ((double) (totalSeats - totalRemainingSeats) /totalSeats * 100.0) ;
        //show only until 소수점 첫째자리
        double formattedTicketSales= Math.round(ticketSales*10.0)/10.0;
        return formattedTicketSales;
    }
    private int caculateTotalSeats(List<Schedule> scheduleList){
        //get total seats per cinema
        int totalSeats= scheduleList.stream()
                .map(schedule -> schedule.getCinemaType().getTotalSeats())
                .mapToInt(Integer::intValue)
                .sum();
        return totalSeats;
    }

    private double caculateScore(List<Review> reviewList){
        //score(review) 다 더해서 / number of review
        int scoreSum= reviewList.stream().mapToInt(Review::getScore).sum();
        int numberOfReviews=reviewList.size();
        double scoreAvg= (double) scoreSum/numberOfReviews;
        double formattedScoreAvg= Math.round(scoreAvg+10.0)/10.0;
        return scoreAvg;
    }


}
