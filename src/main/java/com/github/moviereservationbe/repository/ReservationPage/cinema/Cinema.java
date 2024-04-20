package com.github.moviereservationbe.repository.ReservationPage.cinema;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of="cinemaId")
@Entity
@ToString
@Table(name = "cinema")
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id")
    private Integer cinemaId;

    @Column(name = "cinema_name",length = 50,nullable = false)
    private String cinemaName;

    @Column(name = "address",length = 50,nullable = false)
    private String address;

    @Column(name = "phone_number",length = 50,nullable = false)
    private String phoneNumber;
}
