package com.github.moviereservationbe.repository.ReservationPage.cinemaType;

import com.github.moviereservationbe.repository.ReservationPage.cinema.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CinemaTypeJpa extends JpaRepository<CinemaType, Integer> {

    @Query(
            "SELECT ct " +
                    "FROM CinemaType ct " +
                    "JOIN FETCH ct.cinema c " +
                    "WHERE c = :cinema AND ct.typeName= :cinemaType "
    )
    Optional<CinemaType> findByCinemaAndCinemaType(Cinema cinema, String cinemaType);
}
