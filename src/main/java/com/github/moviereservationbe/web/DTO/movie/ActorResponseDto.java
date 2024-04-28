package com.github.moviereservationbe.web.DTO.movie;


import com.github.moviereservationbe.repository.MainPage.movie.MovieJpa;
import com.github.moviereservationbe.repository.MainPage.movieActor.MovieActor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ActorResponseDto {
    private Integer actorId;
    private String actorName;
    private Date birthday;
    private String nationality;
    private String job;
    private Integer height;

    public ActorResponseDto(Integer actorId, String actorName, Date birthday,
                            String nationality, String job, Integer height) {
        this.actorId = actorId;
        this.actorName = actorName;
        this.birthday = birthday;
        this.nationality = nationality;
        this.job = job;
        this.height = height;
    }
}
