package com.github.moviereservationbe.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpa extends JpaRepository<User, Integer> {


    @Query(
            "SELECT u " +
                    "FROM User u " +
                    "JOIN FETCH u.userRoleList ur " +
                    "JOIN FETCH ur.role r " +
                    "WHERE u.myId = ?1 "
    )
    Optional<User> findBymyIdFetchJoin(String myId);

    boolean existsByMyId(String myId);
}
