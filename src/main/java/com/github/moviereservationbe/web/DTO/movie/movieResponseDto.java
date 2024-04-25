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
    private Integer ticketSales;
    private Integer scoreAvg;
    //private Integer ageLimit;
    //private Integer screenTime;
    //private String country;
    //private String director;
    //private String genre;
    private Integer dDay;
    //private String status; //예매중, 현재상영중, 상영종료
    //private String summary;

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
