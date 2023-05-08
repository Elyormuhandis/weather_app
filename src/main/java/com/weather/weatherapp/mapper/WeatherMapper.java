package com.weather.weatherapp.mapper;

import com.weather.weatherapp.model.Weather;
import com.weather.weatherapp.rest.api.WeatherResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    WeatherResource toResource(Weather weather);

}
