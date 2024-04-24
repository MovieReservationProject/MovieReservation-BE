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
public class ActorResponseDto {
    @JsonProperty("actor-name")
    private String actorName;
    @JsonProperty("birthday")
    private Date birthday;
    @JsonProperty("nationality")
    private String nationality;
    @JsonProperty("job")
    private String job;
    @JsonProperty("height")
    private Integer height;
}
