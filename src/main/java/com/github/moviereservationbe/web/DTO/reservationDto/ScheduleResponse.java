package com.github.moviereservationbe.web.DTO.reservationDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleResponse {
    private String movieName;
    private String cinemaName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private String startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HHmm")
    private String startTime;
    private Integer remainingSeat;
    private String cinemaType;
    private String moviePoster;
}