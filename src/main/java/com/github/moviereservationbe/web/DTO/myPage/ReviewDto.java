package com.github.moviereservationbe.web.DTO.myPage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    @JsonProperty("score")
    private Integer score;
    @JsonProperty("content")
    private String content;
    @JsonProperty("review-date")
    private Date reviewDate;

    public ReviewDto(Integer score, String content) {
        this.score = score;
        this.content = content;
    }
}
