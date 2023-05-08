package com.weather.weatherapp.exception;


public class CityNotFoundException extends NotFoundException {


    public CityNotFoundException(Long id) {
        super(String.format("City [%d] is not found", id));
    }

}
