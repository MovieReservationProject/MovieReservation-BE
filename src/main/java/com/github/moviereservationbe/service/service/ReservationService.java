package com.github.moviereservationbe.service.service;

import com.github.moviereservationbe.repository.Auth.user.User;
import com.github.moviereservationbe.repository.Auth.user.UserJpa;
import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.repository.MainPage.movie.MovieJpa;
import com.github.moviereservationbe.repository.ReservationPage.cinema.Cinema;
import com.github.moviereservationbe.repository.ReservationPage.cinema.CinemaJpa;
import com.github.moviereservationbe.repository.ReservationPage.cinemaType.CinemaType;
import com.github.moviereservationbe.repository.ReservationPage.cinemaType.CinemaTypeJpa;
import com.github.moviereservationbe.repository.ReservationPage.reservation.Reservation;
import com.github.moviereservationbe.repository.ReservationPage.reservation.ReservationJpa;
import com.github.moviereservationbe.repository.ReservationPage.schedule.Schedule;
import com.github.moviereservationbe.repository.ReservationPage.schedule.ScheduleJpa;
import com.github.moviereservationbe.service.exceptions.BadRequestException;
import com.github.moviereservationbe.service.exceptions.ExpiredException;
import com.github.moviereservationbe.service.exceptions.NotFoundException;
import com.github.moviereservationbe.service.exceptions.SoldOutException;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationRequestDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final UserJpa userJpa;
    private final MovieJpa movieJpa;
    private final CinemaJpa cinemaJpa;
    private final CinemaTypeJpa cinemaTypeJpa;
    private final ScheduleJpa scheduleJpa;
    private final ReservationJpa reservationJpa;
    public ResponseDto register(CustomUserDetails customUserDetails, ReservationRequestDto reservationRequestDto) {
        String movieName= reservationRequestDto.getMovieName();
        String cinemaName= reservationRequestDto.getCinemaName();
        String cinemaType= reservationRequestDto.getCinemaType();
        LocalDate movieDate= reservationRequestDto.getMovieDate();
        LocalTime movieTime= reservationRequestDto.getMovieTime();

        //1. find user, movie
        User user= userJpa.findByMyIdFetchJoin(customUserDetails.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with ID: "+ customUserDetails.getMyId()));
        Movie movie= movieJpa.findByTitleKorean(movieName)
                .orElseThrow(()-> new NotFoundException("Cannot find movie with name: "+ movieName));
        //2. if movie status "상영종료" throw ExpiredException
        if(movie.getStatus().equals("상영종료")) throw new ExpiredException("This movie is expired.");

        //3. find cinema with cinemaName
        Cinema cinema = cinemaJpa.findByCinemaName(cinemaName)
                .orElseThrow(()-> new NotFoundException("Cannot find cinema with name: "+ cinemaName));
        //4. find cinemaType with cinemaType and cinema
        CinemaType cinemaAndCinemaType= cinemaTypeJpa.findByCinemaAndCinemaType(cinema, cinemaType)
                .orElseThrow(()-> new NotFoundException("Cannot find cinema type matching cinema and cinema type."));

        //5. if movieDate, movieTime이 reservation보다 먼저이면 throw Exception
        LocalDateTime movieDateTime= movieDate.atTime(movieTime);
        if(movieDateTime.isBefore(LocalDateTime.now())) throw new BadRequestException("Choose movie later than now");


//        //6. (방법1) find schedule with cinemaType, movieName, movieDate, movieTime entity 통째로 찾기
        Schedule schedule= scheduleJpa.findByMovieCinemaTypeStartTime(movie, cinemaAndCinemaType, movieDateTime)
                .orElseThrow(()-> new NotFoundException("Cannot find schedule matching movie, cinema type and start time"));

//        //6. (방법2) find schedule with cinemaTypeId, movieID, movieDateTime 아이디로 찾기
//        Integer movieId= movie.getMovieId();
//        Integer cinemaAndCinemaTypeId= cinemaAndCinemaType.getCinemaTypeId();
//        Schedule schedule= scheduleJpa.findScheduleByMovieIdCinemaTypeIdMovieDateTime(movieId, cinemaAndCinemaTypeId, movieDateTime)
//                .orElseThrow(()-> new NotFoundException("Cannot find schedule matching movieId, cinema type Id and start time"));


        //7. if remainingSeats < 1 throw SoldOutException
        if(schedule.getRemainingSeats()<1) throw new SoldOutException("No more seats available in this movie. Choose another time or cinema.");
        //8. make reserveNum
        ////8-1. cinema number
        String cinemaNum= cinema.getPhoneNumber().substring(cinema.getPhoneNumber().length()-4);
        ////8-2. Date number
        String month = String.format("%02d", movieDate.getMonthValue());
        String day = String.format("%02d", movieDate.getDayOfMonth());
        String dateNum = month + day;
        ////8-3. ramdom number
        Random random = new Random();
        int randomNumberFour = random.nextInt(9000) + 1000;
        int randomNumberThree= random.nextInt(900) + 100;
        ////8-4. reserveNum all together
        String reserveNum= cinemaNum+ "-" + dateNum+ "-" +randomNumberFour+ "-" +randomNumberThree;
        try{
            //9. build reservation
            Reservation reservation= Reservation.builder()
                    .user(user)
                    .reserveNum(reserveNum)
                    .reserveTime(LocalDateTime.now())
                    .schedule(schedule)
                    .build();
            reservationJpa.save(reservation);
            //10. schedule remainingSeats-1
            schedule.setRemainingSeats(schedule.getRemainingSeats()-1);
            scheduleJpa.save(schedule);
            //11. Reservation Response
            ReservationResponseDto reservationResponseDto= ReservationResponseDto
                    .builder()
                    .reserveNum(reservation.getReserveNum())
                    .movieName(reservation.getSchedule().getMovie().getTitleKorean())
                    .cinemaName(reservation.getSchedule().getCinemaType().getCinema().getCinemaName())
                    .reserveDate(reservation.getSchedule().getStartTime().toLocalDate())
                    .reserveTime(reservation.getSchedule().getStartTime().toLocalTime())
                    .reservationAt(reservation.getReserveTime())
                    .build();

            return new ResponseDto(HttpStatus.OK.value(), "Reservation successful", reservationResponseDto);
        }catch(Exception e){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "Reservation fail");
        }
    }

}
