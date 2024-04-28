package com.github.moviereservationbe.repository.MainPage.actor;

import com.github.moviereservationbe.repository.MainPage.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorJpa extends JpaRepository<Actor, Integer> {
//    List<Actor> findAllByMovie(Movie movie);
}
