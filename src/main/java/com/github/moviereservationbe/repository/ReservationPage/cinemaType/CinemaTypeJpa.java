package com.github.moviereservationbe.repository.ReservationPage.cinemaType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaTypeJpa extends JpaRepository<CinemaType, Integer> {
}
