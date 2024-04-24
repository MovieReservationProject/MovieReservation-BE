package com.github.moviereservationbe.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewJpa extends JpaRepository<Review, Integer> {
    @Query(
            "SELECT r " +
                    "FROM Review r " +
                    "JOIN FETCH r.movie m " +
                    "WHERE m.movieId= :movieId"
    )
    List<Review> findByMovieId(Integer movieId);
}
