package com.github.moviereservationbe.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewJpa extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.movie.movieId = :movieId")
    Optional<Review> findByUserIdAndMovieId(@Param("userId") Integer userId, @Param("movieId") Integer movieId);

    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.reviewId = :reviewId")
    Optional<Review> findByUserIdAndReviewId(@Param("userId") Integer userId, @Param("reviewId") Integer reviewId);
}



