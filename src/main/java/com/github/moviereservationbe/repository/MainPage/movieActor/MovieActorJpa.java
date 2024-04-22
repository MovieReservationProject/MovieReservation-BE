package com.github.moviereservationbe.repository.MainPage.movieActor;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieActorJpa extends JpaRepository<MovieActor, Integer> {
}
