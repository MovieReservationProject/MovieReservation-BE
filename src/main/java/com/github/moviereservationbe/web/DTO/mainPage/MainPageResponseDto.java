package com.github.moviereservationbe.web.DTO.mainPage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MainPageResponseDto {
    @JsonProperty("movie-poster")
    private String moviePoster;
    @JsonProperty("title-korean")
    private String titleKorean;
    @JsonProperty("ticket-sales")
    private Double ticketSales;
    @JsonProperty("release-date")
    private Date releaseDate;
    @JsonProperty("score-avg")
    private Double scoreAvg;
    @JsonProperty("D-day")
    private Integer dDay;
}
