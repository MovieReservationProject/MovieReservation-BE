package com.github.moviereservationbe.repository.MainPage.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieJpa extends JpaRepository<Movie, Integer> {
    Optional<Movie> findByName(String movieName);
}
