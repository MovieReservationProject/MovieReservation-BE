package com.github.moviereservationbe.repository.Auth.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialUserJpa extends JpaRepository<SocialUser, Integer> {
}
