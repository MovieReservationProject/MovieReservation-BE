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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieJpa movieJpa;
    private final ActorJpa actorJpa;

    public Page<Movie> getMovies(int page, int size, String sort) {
        //1. 리스트 조회하기 전에 평점, 예매율 계산해서 저장하기
        // 예매율 = (1- remainingseats/totalseats) *100
        movieJpa.updateTicketSales();
        // 평점 = totalscore/reviewcount
        movieJpa.updateScoreAvg();

        movieJpa.updateD_Day();

        //2. 리스트 조회
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sort.equals("1") ? "ticketSales" : "scoreAvg").descending());
        return movieJpa.findAll(pageRequest);
    }

    public ResponseDto movieDetails(Integer movieId) {
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

        return new ResponseDto(HttpStatus.OK.value(), "페이지 조회 성공", movieDetailResponseDto);
        }
    }


