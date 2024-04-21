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
import com.github.moviereservationbe.service.exceptions.SoldOutException;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationRequest;
import com.github.moviereservationbe.web.DTO.reservation.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReservationService {
    private final ScheduleJpa scheduleJpa;
    private final MovieJpa movieJpa;
    private final CinemaJpa cinemaJpa;
    private final CinemaTypeJpa cinemaTypeJpa;
    private final UserJpa userJpa;
    private final ReservationJpa reservationJpa;

    public ResponseDto ticketReservationResult(CustomUserDetails customUserDetails, ReservationRequest reservationRequest) {
        Integer userId = customUserDetails.getUserId();
        String cinemaName = reservationRequest.getCinemaName();
        String cinemaType = reservationRequest.getCinemaType();
        LocalDateTime movieDate = reservationRequest.getMovieDate();
        LocalDateTime movieTime = reservationRequest.getMovieTime();

        User findUser = userJpa.findById(userId)
                .orElseThrow(()-> new NotFoundException("유저 ID("+userId+")에 해당하는 User는 없습니다."));
        Movie findMovie = movieJpa.findByTitleKorean(reservationRequest.getMovieName())
                .orElseThrow(()-> new com.github.moviereservationbe.service.exceptions.NotFoundException("요청하신 영화 이름에 대한 영화는 없습니다."));
        Cinema findCinema = cinemaJpa.findByCinemaName(cinemaName)
                .orElseThrow(()-> new com.github.moviereservationbe.service.exceptions.NotFoundException("요청하신 영화관에 대한 영화관은 없습니다."));
        CinemaType findCinemaType =cinemaTypeJpa.findByCinemaAndTypeName(findCinema,cinemaType)
                .orElseThrow(()-> new com.github.moviereservationbe.service.exceptions.NotFoundException("요청하시는 상영관은 영화관()에 있지 않습니다. "));
        LocalDateTime timeToWatchAMovie =LocalDateTime.of(movieDate.toLocalDate(),movieTime.toLocalTime());

        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyy-MMdd");
        DateTimeFormatter timePattern = DateTimeFormatter.ofPattern("HH:mm");
        Random random = new Random();
        Integer randomNumber = random.nextInt(10000); // 0부터 9999까지의 랜덤 숫자 생성
        String formattedCinemaId = String
                .format("%04d-%04d", findCinema.getCinemaId()+findCinemaType.getCinemaTypeId(),randomNumber);
        LocalDateTime now = LocalDateTime.now();
        String reserveNumber = now.format(datePattern)+"-"+formattedCinemaId;
        // 1. requestBody를 통해 스케줄을 찾는다.

        Schedule schedule =scheduleJpa.findByCinemaTypeAndMovieAndStartTime(findCinemaType,findMovie,timeToWatchAMovie)
                .orElseThrow(()->
                        new NotFoundException("주신 정보("+cinemaName+","+cinemaType+","+timeToWatchAMovie+") 대한 Ticket은 없습니다."));

        if (schedule.getRemainingSeats()<1){
            throw new SoldOutException("남은 좌석이 없습니다. 다른 시간을 이용해 주세요");
        }
        try{
            // 2. reservation DB에 저장(스케줄 번호, userId 저장, 저장할 때 now 시간도 저장)
            Reservation reservation = Reservation.builder()
                    .user(findUser)
                    .reserveNum(reserveNumber)
                    .reserveTime(now)
                    .schedule(schedule)
                    .build();
            Reservation saveReservation =reservationJpa.save(reservation);
            // 3. 해당 스케줄 번호에서 남은 좌석 1빼기
            Schedule reservationSchedule =saveReservation.getSchedule();
            reservationSchedule.setRemainingSeats(schedule.getRemainingSeats()-1);

            ReservationResponse reservationResponse = ReservationResponse.builder()
                    .movieName(reservation.getSchedule().getMovie().getTitleKorean())
                    .cinemaName(reservation.getSchedule().getCinemaType().getCinema().getCinemaName())
                    .reserveDate(reservation.getSchedule().getStartTime().format(datePattern))
                    .reserveTime(reservation.getSchedule().getStartTime().format(timePattern))
                    .reservationAt(saveReservation.getReserveTime())
                    .build();

            return new ResponseDto(HttpStatus.OK.value(),"예약에 성공하셨습니다.",reservationResponse);
        }catch (Exception e){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(),"예약 실패하였습니다.");
        }


    }

    public ResponseDto findMovieByTitle(ReservationRequest reservationRequest) {
        Movie movie =movieJpa.findById(1).orElseThrow(()->new com.github.moviereservationbe.service.exceptions.NotFoundException("영화를 찾을 수 없습니다."));
        return new ResponseDto(HttpStatus.OK.value(), "영화 찾기 성공",movie.getTitleKorean());
    }
}
