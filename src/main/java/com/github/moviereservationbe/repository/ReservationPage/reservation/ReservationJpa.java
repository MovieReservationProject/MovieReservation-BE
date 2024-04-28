package com.github.moviereservationbe.repository.ReservationPage.reservation;

import com.github.moviereservationbe.repository.Auth.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationJpa extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findByReserveIdAndUser(Integer reservationId, User user);
}
