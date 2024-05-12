package com.github.moviereservationbe.web.DTO.MyPage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageReservationResponse {
    private Integer reserveId;
    private String reserveNum;
    private String titleKorean;
    private String titleEnglish;
    private String cinemaName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reserveTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate movieDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
}
