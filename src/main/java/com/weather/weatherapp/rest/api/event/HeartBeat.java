package com.weather.weatherapp.rest.api.event;

import lombok.Data;

import java.util.UUID;

@Data
public class HeartBeat implements Event {

    private String id = UUID.randomUUID().toString();

}
