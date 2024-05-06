package com.github.moviereservationbe.service.service;

import com.github.moviereservationbe.repository.MainPage.actor.Actor;
import com.github.moviereservationbe.repository.MainPage.actor.ActorJpa;
import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import com.github.moviereservationbe.repository.MainPage.movie.MovieJpa;
import com.github.moviereservationbe.service.exceptions.NotFoundException;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.movie.ActorResponseDto;
import com.github.moviereservationbe.web.DTO.movie.MovieDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieJpa movieJpa;
    private final ActorJpa actorJpa;

    public ResponseDto getMovies(int page, int size, String sort) {
        movieJpa.updateTicketSales();
        movieJpa.updateScoreAvg();
        movieJpa.updateD_Day();

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sort.equals("1") ? "ticketSales" : "scoreAvg").descending());
        Page<Movie> moviePage = movieJpa.findAll(pageRequest);
        List<Movie> movies = moviePage.getContent();

        if (movies.stream().toList().isEmpty()) {
            return new ResponseDto(400, "페이지 조회 실패");
        } else {
            List<Object> moviesDetail = new ArrayList<>();

            for (Movie movie : movies) {
                MovieDetailResponseDto movieDetailResponseDto = movieDetails(movie.getMovieId());

                moviesDetail.add(movieDetailResponseDto);
            }

            return new ResponseDto(HttpStatus.OK.value(), "페이지 조회 성공", moviesDetail);
        }
    }

    public MovieDetailResponseDto movieDetails(Integer movieId) {
        Movie movie = movieJpa.findById(movieId)
                .orElseThrow(()-> new NotFoundException("페이지 조회 실패"));

        List<Actor> actors = actorJpa.selectActor(movieId);
        List<ActorResponseDto> actorResponseDtoList= actors
                .stream()
                .map(a-> new ActorResponseDto(
                        a.getActorId(),
                        a.getActorName(),
                        a.getBirthday(),
                        a.getNationality(),
                        a.getJob(),
                        a.getHeight()))
                .collect(Collectors.toList());

        MovieDetailResponseDto movieDetailResponseDto = MovieDetailResponseDto
                .builder()
                .movieId(movie.getMovieId())
                .titleKorean(movie.getTitleKorean())
                .titleEnglish(movie.getTitleEnglish())
                .poster(movie.getPoster())
                .releaseDate(movie.getReleaseDate())
                .ticketSales(movie.getTicketSales())
                .scoreAvg(movie.getScoreAvg())
                .dDay(movie.getDDay())
                .ageLimit(movie.getAgeLimit())
                .screenTime(movie.getScreenTime())
                .country(movie.getCountry())
                .director(movie.getDirector())
                .genre(movie.getGenre())
                .status(movie.getStatus())
                .summary(movie.getSummary())
                .actorResponseDtoList(actorResponseDtoList)
                .build();

        return movieDetailResponseDto;
    }
}


