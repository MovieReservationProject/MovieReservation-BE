package com.github.moviereservationbe.service.exceptions;

public class SoldOutException  extends RuntimeException{
    public SoldOutException(String message) {super(message);}
}
