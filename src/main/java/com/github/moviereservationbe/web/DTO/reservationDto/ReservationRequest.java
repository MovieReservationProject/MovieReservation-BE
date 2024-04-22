package com.github.moviereservationbe.web.DTO.reservationDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationRequest {
    @JsonProperty("movie-name")
    private String movieName;
    @JsonProperty("cinema-name")
    private String cinemaName;
    @JsonProperty("cinema-type")
    private String cinemaType;
    @JsonProperty("reserve-date")
    private LocalDate reserveDate;
    @JsonProperty("reserve-time")
    private LocalTime reserveTime;
}
