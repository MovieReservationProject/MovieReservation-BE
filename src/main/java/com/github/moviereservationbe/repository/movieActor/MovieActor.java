package com.github.moviereservationbe.repository.movieActor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.moviereservationbe.repository.actor.Actor;
import com.github.moviereservationbe.repository.movie.Movie;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "movie_actor")
public class MovieActor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "movie_actor_id")
    private Integer movieActorId;

    @ManyToOne
    @Column(name= "movie_id")
    private Movie movie;

    @Column(name= "actor_id")
    private Actor actor;
}
