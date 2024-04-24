package com.github.moviereservationbe.repository.MainPage.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieJpa extends JpaRepository<Movie, Integer> {
}
