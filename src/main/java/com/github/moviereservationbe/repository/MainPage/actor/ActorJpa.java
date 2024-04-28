package com.github.moviereservationbe.repository.MainPage.actor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorJpa extends JpaRepository<Actor, Integer> {
}
