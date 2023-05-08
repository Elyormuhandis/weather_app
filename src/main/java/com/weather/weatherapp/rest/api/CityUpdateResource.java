package com.weather.weatherapp.rest.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class CityUpdateResource {

    @NotBlank
    @Size(max=4000)
    private String description;

    @NotNull
    private Boolean enabled;

    private Long weatherId;

    private Set<Long> personIds;
}
