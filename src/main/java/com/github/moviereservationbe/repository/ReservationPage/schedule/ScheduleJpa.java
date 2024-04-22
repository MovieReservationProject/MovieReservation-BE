package com.github.moviereservationbe.repository.ReservationPage.schedule;

import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.repository.ReservationPage.cinemaType.CinemaType;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleJpa extends JpaRepository<Schedule, Integer> {
    Optional<Schedule> findByCinemaTypeAndMovieAndStartTime(CinemaType cinemaType, Movie movie, LocalDateTime startTime);

    List<Schedule> findByMovieAndCinemaType(Movie movie,CinemaType cinemaType);

}
