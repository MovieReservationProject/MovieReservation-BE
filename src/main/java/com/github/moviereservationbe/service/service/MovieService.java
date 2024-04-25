package com.github.moviereservationbe.service.service;

import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.repository.MainPage.movie.MovieJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieJpa movieJpa;

    public Page<Movie> getMovies(int page, int size, String sort) {
        //1. 리스트 조회하기 전에 평점, 예매율 계산해서 저장하기
        // 예매율 = remainingseats/totalseats *100
        movieJpa.updateTicketSales();
        // 평점 = totalscore/reviewcount
        movieJpa.updateScoreAvg();

        movieJpa.updateD_Day();

        //2. 리스트 조회
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sort.equals("1") ? "ticketSales" : "scoreAvg").descending());
        return movieJpa.findAll(pageRequest);
    }

}

