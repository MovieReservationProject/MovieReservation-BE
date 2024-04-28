package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.repository.MainPage.movie.MovieJpa;
import com.github.moviereservationbe.service.service.MovieService;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.movie.movieResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MovieController {
    private final MovieService movieService;
    private final MovieJpa movieJpa;
    @GetMapping("/find")
    public ResponseDto getMovies(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                 @RequestParam(value = "size", defaultValue = "3", required = false) int size,
                                 @RequestParam(value = "sort", defaultValue = "1", required = false) String sort){
        Page<Movie> moviePage = movieService.getMovies(page, size, sort);

        List<Movie> movies = moviePage.getContent();

        if (movies.stream().toList().isEmpty()) {
            return new ResponseDto(400, "페이지 조회 실패");
        } else {
            List<movieResponseDto> movieResponseDto = movies
                    .stream()
                    .map(m -> new movieResponseDto(
                            m.getMovieId(),
                            m.getTitleKorean(),
                            m.getTitleEnglish(),
                            m.getPoster(),
                            m.getReleaseDate(),
                            m.getTicketSales(),
                            m.getScoreAvg(),
                            m.getDDay()))
                    .collect(Collectors.toList());

            return new ResponseDto(HttpStatus.OK.value(), "페이지 조회 성공", movieResponseDto);
        }
    }
    @GetMapping("/check/{movieId}")
    public ResponseDto movieDetails(@PathVariable Integer movieId) {
        try {
            return movieService.movieDetails(movieId);
        } catch (NotFoundException e) {
            return new ResponseDto(400, "페이지 조회 실패");
        }
    }

}