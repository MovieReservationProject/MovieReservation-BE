package com.github.moviereservationbe.web.DTO.MyPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageReservationResponse {
    private String reserveNum;
    private String titleKorean;
    private String titleEnglish;
    private String cinemaName;
    private LocalDateTime reserveTime;
}
