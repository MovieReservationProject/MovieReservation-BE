package com.github.moviereservationbe.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewJpa extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.movie.movieId = :movieId")
    Optional<Review> findByUserIdAndMovieId(@Param("userId") Integer userId, @Param("movieId") Integer movieId);

    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.reviewId = :reviewId")
    Optional<Review> findByUserIdAndReviewId(@Param("userId") Integer userId, @Param("reviewId") Integer reviewId);

    @Query("SELECT r FROM Review r WHERE r.movie.movieId = :movieId")
    Optional<Review> findByMovieId(int movieId);
    //movie 관점에서 보면 user(N)-review(N)-movie(1), 그러니까 userid와 같이 찾을 필요없이 movieId만 있으면 userid를 특정할 수 있음

    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId")
    Page<Review> findAllByUserId(int userId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.movie.movieId = :movieId")
    Page<Review> findAllReviewsByMovieId(@Param("movieId") Integer movieId, Pageable pageable);}



