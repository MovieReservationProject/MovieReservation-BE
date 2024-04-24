package com.github.moviereservationbe.repository.MainPage.actor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorJpa extends JpaRepository<Actor, Integer> {
    @Query(
            "SELECT a " +
                    "FROM Actor a " +
                    "JOIN FETCH a.movieActorList ma " +
                    "JOIN FETCH ma.movie m " +
                    "WHERE m.movieId = :movieId"
    )
    List<Actor> findByMovieId(Integer movieId);
}
