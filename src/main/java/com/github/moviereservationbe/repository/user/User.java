package com.github.moviereservationbe.repository.user;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="userId")
@Table(name= "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "user_id")
    private Integer userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name= "my_id", nullable = false)
    private String myId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birthday", nullable = false)
    private String birthday;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

}
