package com.github.moviereservationbe.repository.ReservationPage.cinemaType;

import com.github.moviereservationbe.repository.ReservationPage.cinema.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CinemaTypeJpa extends JpaRepository<CinemaType, Integer> {
    Optional<CinemaType> findByCinemaAndTypeName(Cinema cinema, String typeName);
}