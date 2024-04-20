package com.github.moviereservationbe.repository.ReservationPage.movieCinema;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of="movieCinemaId")
@Entity
@ToString
@Table(name = "movie_cinema")
public class MovieCinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_cinema_id")
    private Integer movieCinemaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id",nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_time_cinema_id",nullable = false)
    private DateTimeCinema dateTimeCinema;
}
