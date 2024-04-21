package com.github.moviereservationbe.repository.ReservationPage.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationJpa extends JpaRepository<Reservation, Integer> {
}
