package com.github.moviereservationbe.repository.ReservationPage.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationJpa extends JpaRepository<Reservation, Integer> {

    @Query(
            "SELECT r " +
                    "FROM Reservation r " +
                    "WHERE r.reserveNum = :reserveNum"
    )
    Optional<Reservation> findByReserveNum(String reserveNum);
}
