package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.service.service.MovieService;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/find")
    public ResponseDto getMovies(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                 @RequestParam(value = "size", defaultValue = "3", required = false) int size,
                                 @RequestParam(value = "sort", defaultValue = "1", required = false) String sort){
        try {
            return movieService.getMovies(page, size, sort);
        } catch (NotFoundException e) {
            return new ResponseDto(400, "페이지 조회 실패");
        }
    }

    @GetMapping("/check/{movieId}")
    public ResponseDto movieDetails(@PathVariable Integer movieId) {
        try {
            return new ResponseDto(200, "페이지 조회 성공", movieService.movieDetails(movieId));
        } catch (NotFoundException e) {
            return new ResponseDto(400, "페이지 조회 실패");
        }
    }
}