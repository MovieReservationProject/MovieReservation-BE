package com.github.moviereservationbe.web.DTO.MyPage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewDate;
}
