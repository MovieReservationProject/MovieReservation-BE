package com.github.moviereservationbe.web.DTO.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
    @JsonProperty("reserve-num")
    private String reserveNum;
    @JsonProperty("movie-name")
    private String movieName;
    @JsonProperty("cinema-name")
    private String cinemaName;
    @JsonProperty("reserve-date")
    private LocalDate reserveDate;
    @JsonProperty("reserve-time")
    private LocalTime reserveTime;
    @JsonProperty("reservation-at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationAt;
}
