package com.github.moviereservationbe.repository.ReservationPage.cinema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CinemaJpa extends JpaRepository<Cinema, Integer> {
    Optional<Cinema> findByCinemaName(String cinemaName);
}
