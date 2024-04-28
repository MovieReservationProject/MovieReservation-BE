package com.github.moviereservationbe.repository.MainPage.actor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorJpa extends JpaRepository<Actor, Integer> {
    @Query(
            value = "SELECT C.* " +
                    "FROM movie AS A " +
                    "INNER JOIN movie_actor AS B ON A.movie_id = B.movie_id " +
                    "INNER JOIN actor AS C ON B.actor_id = C.actor_id " +
                    "WHERE A.movie_id = ?1 "
            , nativeQuery = true)
    List<Actor> selectActor(Integer movieId);
}
