package com.weather.weatherapp.rest.api;

import com.weather.weatherapp.model.Person;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class CityResource {

    private Long id;
    private Long version;

    private String description;
    private Boolean enabled;

    private WeatherResource weather;
    private List<Person> persons;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}
