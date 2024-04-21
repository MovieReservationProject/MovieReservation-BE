package com.github.moviereservationbe.web.DTO.reservation;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationResponse {

    private String movieName;
    private String cinemaName;
    private String reserveDate;
    private String reserveTime;
    private LocalDateTime reservationAt;
}
