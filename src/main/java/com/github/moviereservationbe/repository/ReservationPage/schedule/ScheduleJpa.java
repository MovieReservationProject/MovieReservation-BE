package com.github.moviereservationbe.repository.ReservationPage.schedule;

import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleJpa extends JpaRepository<Schedule, Integer> {
}
