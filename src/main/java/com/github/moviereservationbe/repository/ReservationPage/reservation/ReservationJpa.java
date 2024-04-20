package com.github.moviereservationbe.repository.ReservationPage.reservation;

import com.github.moviereservationbe.web.DTO.MyPage.MyPageReservationResponse;
import com.github.moviereservationbe.web.DTO.MyPage.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpa extends JpaRepository<Reservation, Integer> {
    Page<MyPageReservationResponse> findAllReservation(Pageable pageable);
}
