package com.github.moviereservationbe.service.exceptions;

public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException(String message) {super(message);}
}
