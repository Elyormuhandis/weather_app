package com.weather.weatherapp.rest.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class NewCityResource {

    @NotBlank
    @Size(max=4000)
    private String description;

    private Long weatherId;

    private Set<Long> personIds;

}
