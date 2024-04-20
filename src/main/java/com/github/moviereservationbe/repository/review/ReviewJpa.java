package com.github.moviereservationbe.repository.review;

import com.github.moviereservationbe.web.DTO.MyPage.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpa extends JpaRepository<Review, Integer> {
    Page<ReviewResponse> findAllReviews(Pageable pageable);
}
