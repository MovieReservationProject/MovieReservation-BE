package com.github.moviereservationbe.web.DTO.mainPage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static java.lang.Double.NaN;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
public class MovieDetailResponseDto {
    @JsonProperty("movie-poster")
    private String moviePoster;
    @JsonProperty("title-korean")
    private String titleKorean;
    @JsonProperty("title-english")
    private String titleEnglish;
    @JsonProperty("ticket-sales")
    private Double ticketSales;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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

    public MovieDetailResponseDto(String moviePoster, String titleKorean, String titleEnglish, Double ticketSales, Date releaseDate, Double scoreAvg, Integer dDay, Integer ageLimit, Integer screenTime, String country, String director, String genre, String status, String summary, List<String> actorNameList, List<ReviewResponseDto> reviewResponseDtoList) {
        this.moviePoster = moviePoster;
        this.titleKorean = titleKorean;
        this.titleEnglish = titleEnglish;
        this.ticketSales = ticketSales;
        this.releaseDate = releaseDate;
        this.scoreAvg = Double.isNaN(scoreAvg) ? 10: scoreAvg;
        this.dDay = dDay > 0? dDay : 0;
        this.ageLimit = ageLimit;
        this.screenTime = screenTime;
        this.country = country;
        this.director = director;
        this.genre = genre;
        this.status = status;
        this.summary = summary;
        this.actorNameList = actorNameList;
        this.reviewResponseDtoList = reviewResponseDtoList;
    }
}
