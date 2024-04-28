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
import com.github.moviereservationbe.service.exceptions.*;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationRequestDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationResponseDto;
import com.github.moviereservationbe.web.DTO.reservation.ReservationUpdateDto;
import com.github.moviereservationbe.web.DTO.reservation.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;

import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
        LocalDate movieDate= reservationRequestDto.getReserveDate();
        LocalTime movieTime= reservationRequestDto.getReserveTime();

        //1. find user, movie
        User user= userJpa.findByMyIdFetchJoin(customUserDetails.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with ID: "+ customUserDetails.getMyId()));
        Movie movie= movieJpa.findByTitleKorean(movieName)
                .orElseThrow(()-> new NotFoundException("Cannot find movie with name: "+ movieName));
        //2-1. if movie status "상영종료" throw ExpiredException
        if(movie.getStatus().equals("상영종료")) throw new ExpiredException("This movie is expired.");

        //2-2. if user is too young, reservation fail
        Date birthday= user.getBirthday();
        LocalDate localBirthday = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int currentYear= LocalDate.now().getYear();
        int userAge= currentYear- localBirthday.getYear();
        if(movie.getAgeLimit() > userAge) throw new AgeRestrictionException("You are too young to watch this movie");

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

    public ResponseDto update(CustomUserDetails customUserDetails, ReservationUpdateDto reservationUpdateDto) {
        User user= userJpa.findByMyIdFetchJoin(customUserDetails.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with ID: "+ customUserDetails.getMyId()));
        Reservation reservation= reservationJpa.findByReserveNumAndUser(reservationUpdateDto.getReserveNum(), user)
                .orElseThrow(()-> new NotFoundException("Cannot find reservation with reserve num and User: "+ reservationUpdateDto.getReserveNum()));

        //find schedule in reservation
        Schedule beforeSchedule= reservation.getSchedule();
        //⭐️if movie start time in schedule is before now, reservation cannot be changed
        if(beforeSchedule.getStartTime().isBefore(LocalDateTime.now())) throw new ExpiredException("This reservation is expired.");
        Movie movie= beforeSchedule.getMovie();
        CinemaType cinemaType= beforeSchedule.getCinemaType();
        LocalDate movieDate= beforeSchedule.getStartTime().toLocalDate();
        LocalDateTime movieDateTime= movieDate.atTime(reservationUpdateDto.getReserveTime());
        //⭐if there is no more movie on the same date, reservation cannot be changed
        //⭐if reservationUpdateDto.getReserveTime() is before now, reservation cannot be changed
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(movieDateTime.isBefore(LocalDateTime.now())) throw new ExpiredException("Reservation update has to be after "+ LocalDateTime.now().format(formatter));
        //⭐if reservationUpdateDto.getReserveTime() is same, no reservation to change
        if(movieDateTime.equals(beforeSchedule.getStartTime())) throw new BadRequestException("This reservation is the same reservation");

        try {
            //change schedule: remaining seats previous schedule +1
            beforeSchedule.setRemainingSeats(beforeSchedule.getRemainingSeats() + 1);
            scheduleJpa.save(beforeSchedule);

            //find new schedule with reservationUpdateDto.getReserveTime()
            //if reservationUpdateDto.getReserveTime() does not exist, reservation cannot be changed
            Schedule newSchedule = scheduleJpa.findByMovieCinemaTypeStartTime(movie, cinemaType, movieDateTime)
                    .orElseThrow(() -> new NotFoundException("Cannot find schedule matching movie, cinema type and start time"));
            //change my reservation -> schedule
            reservation.setSchedule(newSchedule);
            reservationJpa.save(reservation);
            //change schedule: remaining seats newSchedule -1
            newSchedule.setRemainingSeats(newSchedule.getRemainingSeats() - 1);
            scheduleJpa.save(newSchedule);

            ReservationResponseDto reservationResponseDto = ReservationResponseDto
                    .builder()
                    .reserveNum(reservation.getReserveNum())
                    .movieName(movie.getTitleKorean())
                    .cinemaName(cinemaType.getCinema().getCinemaName())
                    .reserveDate(newSchedule.getStartTime().toLocalDate())
                    .reserveTime(newSchedule.getStartTime().toLocalTime())
                    .reservationAt(LocalDateTime.now())
                    .build();

            return new ResponseDto(HttpStatus.OK.value(), "Reservation update successful", reservationResponseDto);
        }catch(Exception e){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "Reservation update fail");
        }
    }

    public ResponseDto delete(CustomUserDetails customUserDetails, String reserveNum) {
        User user= userJpa.findByMyIdFetchJoin(customUserDetails.getMyId())
                .orElseThrow(()-> new NotFoundException("Cannot find user with ID: "+ customUserDetails.getMyId()));
        Reservation reservation= reservationJpa.findByReserveNumAndUser(reserveNum, user)
                .orElseThrow(()-> new NotFoundException("Cannot find reservation with reserve num and User: "+ reserveNum));
        Schedule schedule= reservation.getSchedule();
        if(schedule.getStartTime().isBefore(LocalDateTime.now())) throw new ExpiredException("This reservation is expired");
        try{
            //remaining seats +1
            schedule.setRemainingSeats(schedule.getRemainingSeats()+1);
            scheduleJpa.save(schedule);
            //delete reservation
            reservationJpa.delete(reservation);
            return new ResponseDto(HttpStatus.OK.value(), "Reservation delete successful");
        }catch(Exception e){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "Reservation delete fail");

        }

    }

    public List<String> findMovieCinemas(String titleKorean) {
        Movie movie= movieJpa.findByTitleKorean(titleKorean)
                .orElseThrow(()-> new NotFoundException("Cannot find movie with name: "+ titleKorean));
        List<Schedule> scheduleList= movie.getScheduleList();
        List<String> cinenaNameList= scheduleList.stream()
                .map(schedule -> schedule.getCinemaType().getCinema().getCinemaName())
                .distinct()
                .collect(Collectors.toList());
        return cinenaNameList;
    }

    public List<LocalDate> findMovieDates(String titleKorean, String cinemaName) {
        Movie movie= movieJpa.findByTitleKorean(titleKorean)
                .orElseThrow(()-> new NotFoundException("Cannot find movie with name: "+ titleKorean));
        List<Schedule> scheduleList= movie.getScheduleList();
        List<LocalDate> movieDateList= scheduleList.stream()
                .filter(schedule -> schedule.getCinemaType().getCinema().getCinemaName().equals(cinemaName))
                .map(schedule -> schedule.getStartTime().toLocalDate())
                .distinct()
                .collect(Collectors.toList());
        return movieDateList;
    }

    public List<ScheduleResponseDto> findMovieTimes(String titleKorean, String cinemaName, LocalDate movieDate) {
        Movie movie= movieJpa.findByTitleKorean(titleKorean)
                .orElseThrow(()-> new NotFoundException("Cannot find movie with name: "+ titleKorean));
        List<Schedule> scheduleList= movie.getScheduleList().stream()
                .filter(schedule -> schedule.getCinemaType().getCinema().getCinemaName().equals(cinemaName))
                .filter(schedule -> schedule.getStartTime().toLocalDate().equals(movieDate))
                .collect(Collectors.toList());

        List<LocalTime> movieTimes = scheduleList.stream().map(schedule -> schedule.getStartTime().toLocalTime()).collect(Collectors.toList());

        List<ScheduleResponseDto> scheduleResponseDtoList = new ArrayList<>();
        for(Schedule s: scheduleList) {
            ScheduleResponseDto scheduleResponseDto = ScheduleResponseDto.builder()
                    .cinemaTypeName(s.getCinemaType().getTypeName())
                    .totalSeats(s.getCinemaType().getTotalSeats())
                    .movieTime(movieTimes)
                    .remainingSeats(s.getRemainingSeats())
                    .build();
            scheduleResponseDtoList.add(scheduleResponseDto);
        }
    return scheduleResponseDtoList;
    }
}
