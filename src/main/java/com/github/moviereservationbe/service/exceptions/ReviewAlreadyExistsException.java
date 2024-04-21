package com.github.moviereservationbe.service.exceptions;

public class ReviewAlreadyExistsException extends Throwable {
    public ReviewAlreadyExistsException(String message) {
        super(message);
    }
}
