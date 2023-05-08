package com.weather.weatherapp.rest.api.event;

import com.weather.weatherapp.rest.api.CityResource;
import lombok.Value;

@Value
public class CitySaved implements Event {

    CityResource city;

}
