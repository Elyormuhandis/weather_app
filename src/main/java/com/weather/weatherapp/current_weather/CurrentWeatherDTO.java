package com.weather.weatherapp.current_weather;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CurrentWeatherDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String main;

    @NotNull
    @Size(max = 255)
    private String description;

    @NotNull
    private Double temperature;

    @NotNull
    private Double visibility;

    @NotNull
    private Double pressure;

    @NotNull
    private Double humidity;

    @NotNull
    private Double windSpeed;

    @NotNull
    private Double windDegree;

}
