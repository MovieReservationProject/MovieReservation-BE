package com.github.moviereservationbe.web.DTO.MyPage;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Integer reviewId;
    private String titleKorean;
    private String cinemaName;
    private LocalDateTime reserveTime;
    private Integer score;
    private String content;
    private LocalDateTime reviewDate;
}
