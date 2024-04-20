package com.github.moviereservationbe.repository.Auth.role;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of= "roleId")
@Table(name= "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "role_id")
    private Integer roleId;

    @Column(name= "name", nullable = false)
    private String name;
}
