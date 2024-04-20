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
public class ReviewResponse {
    private String titleKorean;
    private String cinemaName;
    private LocalDateTime reserveTime;
    private Integer score;
    private String content;
}
