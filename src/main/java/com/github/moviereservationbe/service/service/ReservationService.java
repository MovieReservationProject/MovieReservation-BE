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
import com.github.moviereservationbe.service.exceptions.AgeRestrictionException;
import com.github.moviereservationbe.service.exceptions.ExpiredException;
import com.github.moviereservationbe.service.exceptions.NotFoundException;
import com.github.moviereservationbe.service.exceptions.SoldOutException;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.reservationDto.ReservationRequest;
import com.github.moviereservationbe.web.DTO.reservationDto.ReservationResponse;
import com.github.moviereservationbe.web.DTO.reservationDto.ReservationChange;
import com.github.moviereservationbe.web.DTO.reservationDto.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    public ResponseDto getMovieScheduleList() {
        List<Schedule> scheduleList =scheduleJpa.findAll();
        List<Object> locationTheaters = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            ScheduleResponse scheduleResponse = mapScheduleResponse(schedule);
            locationTheaters.add(scheduleResponse);
        }
        return new ResponseDto(HttpStatus.OK.value(),"조회성공",Map.of("locationTheaters",locationTheaters));
    }

    public ScheduleResponse mapScheduleResponse(Schedule schedule){
        return ScheduleResponse.builder()
                .movieName(schedule.getMovie().getTitleKorean())
                .cinemaType(schedule.getCinemaType().getTypeName())
                .cinemaName(schedule.getCinemaType().getCinema().getCinemaName())
                .startDate(schedule.getStartTime().toLocalDate().toString())
                .startTime(schedule.getStartTime().toLocalTime().toString())
                .remainingSeat(schedule.getRemainingSeats())
                .moviePoster(schedule.getMovie().getPoster())
                .build();

    }
    @Transactional
    public ResponseDto ticketReservationResult(CustomUserDetails customUserDetails, ReservationRequest reservationRequest) {
        Integer userId = customUserDetails.getUserId();
        String movieName=reservationRequest.getMovieName();
        String cinemaName = reservationRequest.getCinemaName();
        String cinemaType = reservationRequest.getCinemaType();
        LocalDate movieDate = reservationRequest.getReserveDate();
        LocalTime movieTime = reservationRequest.getReserveTime();

        User findUser = userJpa.findById(userId)
                .orElseThrow(()-> new NotFoundException("유저 ID("+userId+")에 해당하는 User는 없습니다."));
        Date userBirthday =findUser.getBirthday();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userBirthday);
        LocalDate today = LocalDate.now();
        Integer currentYear = today.getYear();
        Integer birthday = calendar.get(Calendar.YEAR);

        Movie findMovie = movieJpa.findByTitleKorean(movieName)
                .orElseThrow(()-> new com.github.moviereservationbe.service.exceptions.NotFoundException("요청하신 영화 이름에 대한 영화는 없습니다."));
        if (findMovie.getStatus().equals("상영종료")){
            throw new ExpiredException("상영 종료된 영화입니다.");
        }
        if (findMovie.getAgeLimit()>(currentYear-birthday)){
            throw new AgeRestrictionException("해당 영화는 "+findMovie.getAgeLimit()+"살 부터 시청 가능합니다.");
        }
        Cinema findCinema = cinemaJpa.findByCinemaName(cinemaName)
                .orElseThrow(()-> new com.github.moviereservationbe.service.exceptions.NotFoundException("요청하신 영화관에 대한 영화관은 없습니다."));
        CinemaType findCinemaType =cinemaTypeJpa.findByCinemaAndTypeName(findCinema,cinemaType)
                .orElseThrow(()-> new com.github.moviereservationbe.service.exceptions.NotFoundException("요청하시는 상영관은 영화관("+movieName+")에 있지 않습니다. "));
        LocalDateTime timeToWatchAMovie =LocalDateTime.of(movieDate,movieTime);
        log.error("시간 :"+timeToWatchAMovie);
        if (timeToWatchAMovie.isBefore(LocalDateTime.now())){
            throw new ExpiredException("고르신 Ticket의 상영 날짜는 지난 날짜입니다.");
        }
        DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        //예약 번호 생성
        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyy-MMdd");

        Random random = new Random();
        Integer randomNumber = random.nextInt(10000); // 0부터 9999까지의 랜덤 숫자 생성
        String formattedCinemaId = String
                .format("%04d-%04d", findCinema.getCinemaId()+findCinemaType.getCinemaTypeId(),randomNumber);
        LocalDateTime now = LocalDateTime.now();
        String reserveNumber = now.format(datePattern)+"-"+formattedCinemaId;
        // 1. requestBody를 통해 스케줄을 찾는다.
        Schedule schedule =scheduleJpa.findByCinemaTypeAndMovieAndStartTime(findCinemaType,findMovie,timeToWatchAMovie)
                .orElseThrow(()->
                        new NotFoundException("주신 정보("+reservationRequest.getMovieName()+cinemaName+","+cinemaType+","+timeToWatchAMovie.format(dateTimePattern)+") 대한 Ticket은 없습니다."));

        if (schedule.getRemainingSeats()<1){
            throw new SoldOutException("남은 좌석이 없습니다. 다른 시간을 이용해 주세요");
        }
        try{
            // 2. reservation DB에 저장(스케줄 번호, userId 저장, 저장할 때 now 시간도 저장)
            Reservation reservation= new Reservation(findUser,reserveNumber,now,schedule);

            Reservation saveReservation =reservationJpa.save(reservation);
            // 3. 해당 스케줄 번호에서 남은 좌석 1빼기
            Schedule reservationSchedule =saveReservation.getSchedule();
            log.error("테스트: "+ reservationSchedule.getRemainingSeats());
            reservationSchedule.setRemainingSeats(schedule.getRemainingSeats()-1);

            ReservationResponse reservationResponse = new ReservationResponse(saveReservation);

            return new ResponseDto(HttpStatus.OK.value(),"예약에 성공하셨습니다.",reservationResponse);
        }catch (Exception e){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(),"예약 실패하였습니다.");
        }


    }

    @Transactional
    public ResponseDto changeTicketResult(CustomUserDetails customUserDetails, Integer reservationId, ReservationChange reservationChange) {

        Integer userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId).orElseThrow(()-> new NotFoundException("아이디에 해당하는 유저가 존재하지 않습니다."));
        //1. 예약 번호와 유저아이디로 예약한 거 찾기
        Reservation reservation = reservationJpa.findByReserveIdAndUser(reservationId,user)
                .orElseThrow(()-> new NotFoundException("예약 번호에 해당하는 Ticket이 없습니다."));
        //2. 예약한 걸로 관련 스케줄 찾기
        Schedule findSchedule =reservation.getSchedule();
        if (findSchedule.getStartTime().isBefore(LocalDateTime.now())){
            throw new NotFoundException("해당 Ticket은 기간이 지난 Ticket입니다.");
        }
        Movie findMovie = findSchedule.getMovie();
        CinemaType findCinemaType = findSchedule.getCinemaType();
        LocalDateTime findLocalDateTime = findSchedule.getStartTime();
        LocalDate findLocalDate = findLocalDateTime.toLocalDate();
        //3. 예외처리
        LocalDateTime changeTime =LocalDateTime.of(findLocalDate,reservationChange.getChangeTime());
        if (changeTime.isBefore(LocalDateTime.now())){
            throw new ExpiredException("고르신 Ticket의 상영 날짜는 지난 날짜입니다.");
        }
        if (changeTime.equals(findLocalDateTime)){
            throw new ExpiredException("동일한 시간입니다.");
        }
        Schedule changeSchedule = scheduleJpa.findByCinemaTypeAndMovieAndStartTime(findCinemaType,findMovie,changeTime)
                .orElseThrow(()->new NotFoundException("해당 시간의 Ticket은 없습니다."));
        try {

            //4. 찾은 스케줄은 예약 진행하고 좌석 1개 빼기
            reservation.setSchedule(changeSchedule);
            reservation.setReserveTime(LocalDateTime.now());
            changeSchedule.setRemainingSeats(changeSchedule.getRemainingSeats()-1);

            //5. 그 전에 찾은 스케줄에는 좌석 1개 더하기
            findSchedule.setRemainingSeats(findSchedule.getRemainingSeats() + 1);
            ReservationResponse reservationResponse = new ReservationResponse(reservation);

            return new ResponseDto(HttpStatus.OK.value(), "예약 변경에 성공",reservationResponse);
        }catch (Exception e){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "예약 변경 실패");
        }

    }

    public ResponseDto deleteReservation(CustomUserDetails customUserDetails, Integer reservationId) {
        Integer userId = customUserDetails.getUserId();
        User user = userJpa.findById(userId).orElseThrow(() -> new NotFoundException("아이디에 해당하는 유저가 존재하지 않습니다."));
        Reservation reservation = reservationJpa.findByReserveIdAndUser(reservationId,user)
                .orElseThrow(()-> new NotFoundException("예약 내역이 없습니다."));
        Schedule findSchedule =reservation.getSchedule();
        LocalDateTime startTime = findSchedule.getStartTime();

        LocalDateTime currentDateTime = LocalDateTime.now();
        long minuteDifference = ChronoUnit.MINUTES.between(startTime, currentDateTime);

        if (minuteDifference < 10){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "상영 시간 10분 전이라 취소가 불가능합니다.");
        } else{
            reservationJpa.deleteById(reservationId);
            return new ResponseDto(HttpStatus.OK.value(), "예약 취소 완료 되었습니다.");
        }
    }
}
