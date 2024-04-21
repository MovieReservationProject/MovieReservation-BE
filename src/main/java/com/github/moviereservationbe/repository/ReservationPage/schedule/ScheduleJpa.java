package com.github.moviereservationbe.repository.ReservationPage.schedule;

import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.repository.ReservationPage.cinemaType.CinemaType;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

public interface ScheduleJpa extends JpaRepository<Schedule, Integer> {
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
