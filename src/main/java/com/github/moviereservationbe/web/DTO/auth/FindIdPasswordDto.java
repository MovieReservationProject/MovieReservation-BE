package com.github.moviereservationbe.web.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindIdPasswordDto {
    private String myId;
    private String name;
    private String phoneNumber;

    public FindIdPasswordDto(String myId) {
        this.myId = myId;
    }

    public FindIdPasswordDto(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
