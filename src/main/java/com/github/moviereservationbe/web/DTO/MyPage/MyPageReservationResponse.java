package com.github.moviereservationbe.web.DTO.MyPage;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageReservationResponse {
    private String reserveNum;
    private String titleKorean;
    private String titleEnglish;
    private String cinemaName;
    private LocalDateTime reserveTime;
}
