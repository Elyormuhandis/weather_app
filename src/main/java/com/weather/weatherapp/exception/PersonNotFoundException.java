package com.weather.weatherapp.exception;


public class PersonNotFoundException extends NotFoundException {

    public PersonNotFoundException(Long id) {
        super(String.format("Weather [%d] is not found", id));
    }

}
