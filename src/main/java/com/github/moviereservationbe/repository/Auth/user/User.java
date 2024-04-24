package com.github.moviereservationbe.repository.Auth.user;
import com.github.moviereservationbe.repository.Auth.userRole.UserRole;
import com.github.moviereservationbe.repository.ReservationPage.reservation.Reservation;
import com.github.moviereservationbe.repository.review.Review;
import com.github.moviereservationbe.web.DTO.MyPage.MyPageUserDetailRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

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
    private Date birthday;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //cascade, orphanRemoval 추가해보았음
    private List<UserRole> userRoleList;

}
