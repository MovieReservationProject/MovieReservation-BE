package com.github.moviereservationbe.web.DTO.movie;

import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponseDto {
    private Integer movieId;
    private String titleKorean;
    private String titleEnglish;
    private String poster;
    private Date releaseDate;
    private Double ticketSales;
    private Double scoreAvg;
    private Integer dDay;

    public MovieResponseDto(Movie movie) {
        this.movieId = movie.getMovieId();
        this.titleKorean = movie.getTitleKorean();
        this.titleEnglish = movie.getTitleEnglish();
        this.ticketSales = movie.getTicketSales();
        this.releaseDate = movie.getReleaseDate();
        this.dDay = movie.getDDay();
        this.poster = movie.getPoster();
        this.scoreAvg = movie.getScoreAvg();
    }
}

