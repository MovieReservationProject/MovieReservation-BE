package com.github.moviereservationbe.repository.review;

import com.github.moviereservationbe.web.DTO.MyPage.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewJpa extends JpaRepository<Review, Integer> {
    Page<ReviewResponse> findAllReviews(Pageable pageable);
    Optional<Review> findByUserIdAndMovieId(Integer userId, Integer movieId);
}
