package com.github.moviereservationbe.web.DTO.reservation;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDto {
    @JsonProperty("cinema-type-name")
    private String cinemaTypeName;
    @JsonProperty("total-seats")
    private Integer totalSeats;
    @JsonProperty("movie-time")
    private List<LocalTime> movieTime;
    @JsonProperty("remaining-seats")
    private Integer remainingSeats;
}
