package com.weather.weatherapp.current_weather;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/currentWeathers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrentWeatherResource {

    private final CurrentWeatherService currentWeatherService;

    public CurrentWeatherResource(final CurrentWeatherService currentWeatherService) {
        this.currentWeatherService = currentWeatherService;
    }

    @GetMapping
    public ResponseEntity<List<CurrentWeatherDTO>> getAllCurrentWeathers() {
        return ResponseEntity.ok(currentWeatherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrentWeatherDTO> getCurrentWeather(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(currentWeatherService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCurrentWeather(
            @RequestBody @Valid final CurrentWeatherDTO currentWeatherDTO) {
        final Long createdId = currentWeatherService.create(currentWeatherDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCurrentWeather(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CurrentWeatherDTO currentWeatherDTO) {
        currentWeatherService.update(id, currentWeatherDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCurrentWeather(@PathVariable(name = "id") final Long id) {
        currentWeatherService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
