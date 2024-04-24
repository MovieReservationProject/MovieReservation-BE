package com.github.moviereservationbe.web.DTO.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {
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
