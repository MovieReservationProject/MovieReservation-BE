package com.github.moviereservationbe.web.DTO.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("movie-name")
    private String movieName;
    @JsonProperty("cinema-name")
    private String cinemaName;
    @JsonProperty("cinema-type")
    private String cinemaType;
    @JsonProperty("reserve-date")

    private String reserveDate;
    @JsonProperty("reserve-time")

    private String reserveTime;
}
