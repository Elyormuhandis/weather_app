package com.weather.weatherapp.rest.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Optional;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CityPatchResource {

    private Optional<@NotBlank @Size(max=4000) String> description;
    private Optional<@NotNull Boolean> enabled;
    private Optional<Long> weatherId;
    private Optional<Set<Long>> personIds;

}
