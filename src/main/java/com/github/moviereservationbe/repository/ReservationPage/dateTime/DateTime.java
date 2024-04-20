package com.github.moviereservationbe.repository.ReservationPage.dateTime;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of="dateId")
@Entity
@ToString
@Table(name = "date_time")
public class DateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_time_id")
    private Integer dateTimeId;
    @Column(name = "movie_date_time",nullable = false)
    private LocalDateTime movieDateTime;
    @Column(name = "ticket_total",nullable = false)
    private Integer ticketTotal;
}
