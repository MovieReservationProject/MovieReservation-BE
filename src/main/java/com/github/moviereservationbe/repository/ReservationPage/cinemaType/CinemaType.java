package com.github.moviereservationbe.repository.ReservationPage.cinemaType;

import com.github.moviereservationbe.repository.ReservationPage.cinema.Cinema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="cinemaTypeId")
@Table(name= "cinema_type")
public class CinemaType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_type_id")
    private Integer cinemaTypeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cinema_id",nullable = false)
    private Cinema cinema;

    @Column(name = "type_name",nullable = false)
    private String typeName;

    @Column(name = "total_seats",nullable = false)
    private Integer totalSeats;

}
