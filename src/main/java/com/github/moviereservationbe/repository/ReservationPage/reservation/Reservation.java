package com.github.moviereservationbe.repository.ReservationPage.reservation;

import com.github.moviereservationbe.repository.Auth.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of="reserveId")
@Entity
@ToString
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_id")
    private Integer reserveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "reserve_num",length = 25,nullable = false)
    private String reserveNum;

    @Column(name="reserve_time",nullable = false)
    private LocalDateTime reserveTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_cinema_id",nullable = false)
    private MovieCinema movieCinema;
}
