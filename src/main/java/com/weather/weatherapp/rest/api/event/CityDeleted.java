package com.weather.weatherapp.rest.api.event;

import lombok.Value;

@Value
public class CityDeleted implements Event {

    Long cityId;

}
