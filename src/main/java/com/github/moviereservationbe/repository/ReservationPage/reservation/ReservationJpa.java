package com.github.moviereservationbe.repository.ReservationPage.reservation;

import com.github.moviereservationbe.repository.Auth.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface ReservationJpa extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findByReserveIdAndUser(Integer reservationId, User user);

    @Query("SELECT r FROM Reservation r WHERE r.user.userId = :userId")
    Page<Reservation> findAllByUserId(int userId, Pageable pageable);
}