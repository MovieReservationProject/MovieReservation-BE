package com.github.moviereservationbe.web.DTO.reservation;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationRequest {

    private String movieName;
    private String cinemaName;
    private String cinemaType;
    private LocalDateTime movieDate;
    private LocalDateTime movieTime;
}
