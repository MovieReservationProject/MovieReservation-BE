package com.github.moviereservationbe.repository.review;

import com.github.moviereservationbe.repository.Auth.user.User;
import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
@Entity
public class Review {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "review_id")
        private Integer reviewId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "movie_id")
        private Movie movie;

        @Column(name = "score", nullable = false)
        private Integer score;

        @Column(name="content", nullable = false)
        private String content;

        @Column(name = "review_date", nullable = false)
        private LocalDateTime reviewDate;

}
