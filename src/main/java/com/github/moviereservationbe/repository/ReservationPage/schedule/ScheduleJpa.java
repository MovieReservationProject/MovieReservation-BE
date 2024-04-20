package com.github.moviereservationbe.repository.ReservationPage.schedule;

import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleJpa extends JpaRepository<Schedule, Integer> {
}
