package com.github.moviereservationbe.web.DTO.mainPage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailResponseDto {
    @JsonProperty("movie-poster")
    private String moviePoster;
    @JsonProperty("title-korean")
    private String titleKorean;
    @JsonProperty("title-english")
    private String titleEnglish;
    @JsonProperty("ticket-sales")
    private Double ticketSales;
    @JsonProperty("release-date")
    private Date releaseDate;
    @JsonProperty("score-avg")
    private Double scoreAvg;
    @JsonProperty("D-day")
    private Integer dDay;
    @JsonProperty("age-limit")
    private Integer ageLimit;
    @JsonProperty("screen-time")
    private Integer screenTime;
    @JsonProperty("country")
    private String country;
    @JsonProperty("director")
    private String director;
    @JsonProperty("genre")
    private String genre;
    @JsonProperty("status")
    private String status;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("actor")
    private List<String> actorNameList;
    @JsonProperty("review")
    private List<ReviewResponseDto> reviewResponseDtoList;
}
