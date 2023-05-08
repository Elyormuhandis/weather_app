package com.weather.weatherapp.exception;


public class WeatherNotFoundException extends NotFoundException {

    public WeatherNotFoundException(Long id) {
        super(String.format("Person [%d] is not found", id));
    }

}
