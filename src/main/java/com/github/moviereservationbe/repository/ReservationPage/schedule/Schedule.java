package com.github.moviereservationbe.repository.ReservationPage.schedule;

import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.repository.ReservationPage.cinemaType.CinemaType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of="scheduleId")
@Entity
@ToString
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id",nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_type_id",nullable = false)
    private CinemaType cinemaType;

    @Column(name = "start_time",nullable = false)
    private LocalDateTime startTime;

    @Column(name = "remaining_seats",nullable = false)
    private Integer remainingSeats;

}

