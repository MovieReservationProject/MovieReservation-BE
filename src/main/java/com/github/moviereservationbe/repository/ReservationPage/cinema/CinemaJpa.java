package com.github.moviereservationbe.repository.ReservationPage.cinema;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CinemaJpa extends JpaRepository<Cinema, Integer> {
    Optional<Cinema> findByName(String cinemaName);
}
