package com.github.moviereservationbe.web.DTO.movie;

import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class movieResponseDto {
    private Integer movieId;
    private String titleKorean;
    private String titleEnglish;
    private String poster;
    private Date releaseDate;
    private Double ticketSales;
    private Double scoreAvg;
    private Integer dDay;

    public movieResponseDto(Movie movie) {
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
