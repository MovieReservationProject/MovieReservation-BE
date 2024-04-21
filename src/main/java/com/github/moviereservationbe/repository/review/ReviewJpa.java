package com.github.moviereservationbe.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewJpa extends JpaRepository<Review, Integer> {
//    Page<ReviewResponse> findAll(Pageable pageable);
//    Optional<Review> findByUserIdAndMovieId(Integer userId, Integer movieId);
}
