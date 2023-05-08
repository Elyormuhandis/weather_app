package com.weather.weatherapp.rest.api;

import lombok.Data;

@Data
public class WeatherResource {

    private Long id;
    private String description;
    private Integer temperature;

}
