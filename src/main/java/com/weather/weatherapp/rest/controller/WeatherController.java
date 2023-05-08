package com.weather.weatherapp.rest.controller;


import com.weather.weatherapp.mapper.WeatherMapper;
import com.weather.weatherapp.rest.api.WeatherResource;
import com.weather.weatherapp.service.WeatherService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequestMapping(value = "/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;
    private final WeatherMapper weatherMapper;

    @ApiResponse(description = "Find a person by his id")
    @GetMapping(value = "/{id}", produces = {APPLICATION_JSON_VALUE})
    public Mono<WeatherResource> findById(@PathVariable final Long id) {

        return weatherService.findById(id).map(weatherMapper::toResource);
    }

    @ApiResponse(description = "Get the persons")
    @GetMapping(produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<WeatherResource> getAll() {

        return weatherService.findAll()
                .map(weatherMapper::toResource);
    }

}
