package com.github.moviereservationbe.web.DTO.reservationDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.moviereservationbe.repository.ReservationPage.reservation.Reservation;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationResponse {

    private String movieName;
    private String cinemaName;
    private String cinemaType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime reserveTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationAt;



    public ReservationResponse(Reservation reservation) {
        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timePattern = DateTimeFormatter.ofPattern("HH:mm");
        this.movieName=reservation.getSchedule().getMovie().getTitleKorean();
        this.cinemaName=reservation.getSchedule().getCinemaType().getCinema().getCinemaName();
        this.cinemaType=reservation.getSchedule().getCinemaType().getTypeName();
        this.reserveDate=LocalDate.parse(reservation.getSchedule().getStartTime().format(datePattern));
        this.reserveTime=LocalTime.parse(reservation.getSchedule().getStartTime().format(timePattern));
        this.reservationAt=reservation.getReserveTime();
    }
}