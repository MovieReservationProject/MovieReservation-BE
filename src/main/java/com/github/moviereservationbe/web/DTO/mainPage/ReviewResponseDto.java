package com.github.moviereservationbe.web.DTO.mainPage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("score")
    private Integer score;
    @JsonProperty("content")
    private String content;
    @JsonProperty("review-date")
    private Date reviewDate;
}
