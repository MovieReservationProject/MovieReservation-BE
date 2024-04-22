package com.github.moviereservationbe.repository.MainPage.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieJpa extends JpaRepository<Movie, Integer> {
    Optional<Movie> findByTitleKorean(String movieName);
}
