package com.github.moviereservationbe.repository.MainPage.actor;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "actor")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "actor_id")
    private Integer actorId;

    @Column(name= "actor_name" , nullable = false)
    private String actorName;

    @Column(name= "birthday" , nullable = false)
    private Date birthday;

    @Column(name= "nationality" , nullable = false)
    private String nationality;

    @Column(name= "job" , nullable = false)
    private String job;

    @Column(name= "height" , nullable = false)
    private Integer height;


}
