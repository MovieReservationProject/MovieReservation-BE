package com.github.moviereservationbe.repository.MainPage.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MovieJpa extends JpaRepository<Movie, Integer> {
    @Transactional
    @Modifying
    @Query(
            value = "UPDATE movie AS A " +
                    "INNER JOIN schedule AS B ON A.movie_id = B.movie_id " +
                    "INNER JOIN cinema_type AS C ON B.cinema_type_id = C.cinema_type_id " +
                    "SET A.ticket_sales = (C.total_seats - B.remaining_seats) / C.total_seats * 100 "
    , nativeQuery = true)
    void updateTicketSales();

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE movie AS A " +
                    "INNER JOIN (SELECT movie_id, (count(score) / sum(score)) AS avg_score " +
                    "            FROM review group by movie_id) AS B ON A.movie_id = B.movie_id " +
                    " SET A.score_avg = B.avg_score "
    , nativeQuery = true)
    void updateScoreAvg();

    Page<Movie> findAll(Pageable pageable);

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE movie AS A " +
                    "INNER JOIN (SELECT release_date, DATEDIFF (now(), release_date) AS D_DAY " +
                    "             FROM movie) AS B ON A.release_date = B.release_date " +
                    "SET A.d_day = B.D_DAY "
    , nativeQuery = true)
    void updateD_Day();
}
