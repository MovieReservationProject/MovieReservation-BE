package com.github.moviereservationbe.repository.ReservationPage.schedule;

import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.repository.ReservationPage.cinemaType.CinemaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface ScheduleJpa extends JpaRepository<Schedule, Integer> {
    //        //6. (방법2) find schedule with cinemaTypeId, movieID, movieDateTime 아이디로 찾기

//    @Query(
//            "SELECT s " +
//                    "FROM Schedule s " +
//                    "WHERE s.movie.movieId = :movieId " +
//                    "AND s.cinemaType.cinemaTypeId = :cinemaAndCinemaTypeId " +
//                    "AND s.startTime = :movieDateTime "
//
//    )
//    Optional<Schedule> findScheduleByMovieIdCinemaTypeIdMovieDateTime(Integer movieId, Integer cinemaAndCinemaTypeId, LocalDateTime movieDateTime);


    //        //6. (방법1) find schedule with cinemaType, movieName, movieDate, movieTime entity 통째로 찾기
    @Query(
            "SELECT s " +
                    "FROM Schedule s " +
                    "JOIN FETCH s.movie m " +
                    "JOIN FETCH s.cinemaType ct " +
                    "WHERE m = :movie " +
                    "AND ct = :cinemaAndCinemaType " +
                    "AND s.startTime = :movieDateTime " //schedule entity가 localDateTime일 때 가능한 JPQL
    )

    Optional<Schedule> findByMovieCinemaTypeStartTime(Movie movie, CinemaType cinemaAndCinemaType, LocalDateTime movieDateTime);

}
