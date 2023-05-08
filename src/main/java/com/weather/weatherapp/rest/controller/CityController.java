package com.weather.weatherapp.rest.controller;


import com.weather.weatherapp.mapper.CityMapper;
import com.weather.weatherapp.rest.api.CityPatchResource;
import com.weather.weatherapp.rest.api.CityResource;
import com.weather.weatherapp.rest.api.CityUpdateResource;
import com.weather.weatherapp.rest.api.NewCityResource;
import com.weather.weatherapp.rest.api.event.Event;
import com.weather.weatherapp.rest.api.event.CityDeleted;
import com.weather.weatherapp.rest.api.event.CitySaved;
import com.weather.weatherapp.service.CityService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping(value = "/city")
@RequiredArgsConstructor
@Slf4j
public class CityController {

    private final CityService cityService;
    private final CityMapper cityMapper;

    @ApiResponse(description = "Create a new city")
    @PostMapping
    public Mono<ResponseEntity<Void>> create(@Valid @RequestBody final NewCityResource newCityResource) {

        return cityService.create(cityMapper.toModel(newCityResource))
                .map(city -> created(linkTo(CityController.class).slash(city.getId()).toUri()).build());

    }

    @ApiResponse(description = "Update an existing city")
    @PutMapping(value = "/{id}")
    public Mono<ResponseEntity<Void>> update(@PathVariable @NotNull final Long id,
                                             @RequestHeader(value = HttpHeaders.IF_MATCH) final Long version,
                                             @Valid @RequestBody final CityUpdateResource cityUpdateResource) {

        // Find the city and update the instance
        return cityService.findById(id, version, false)
          .map(city -> cityMapper.update(cityUpdateResource, city))
          .flatMap(cityService::update)
          .map(city -> noContent().build());
    }

    @ApiResponse( description="Patch an existing city following the patch merge RCF (https://tools.ietf.org/html/rfc7396)")
    @PatchMapping(value = "/{id}")
    public Mono<ResponseEntity<Void>> patch(@PathVariable @NotNull final Long id,
                                            @RequestHeader(value = HttpHeaders.IF_MATCH) final Long version,
                                            @Valid @RequestBody final CityPatchResource patch) {

        return cityService.findById(id, version, true)
                .map(city -> cityMapper.patch(patch, city))
                .flatMap(cityService::update)
                .map(cityId -> noContent().build());
    }

    @ApiResponse(description="Find an city by its id")
    @GetMapping(value = "/{id}", produces = {APPLICATION_JSON_VALUE})
    public Mono<CityResource> findById(@PathVariable final Long id) {

        return cityService.findById(id, null, true).map(cityMapper::toResource);
    }

    @ApiResponse(description="Get the list of citys")
    @GetMapping(produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<CityResource> getAllcities() {

        return cityService.findAll()
                .map(cityMapper::toResource);
    }

    @ApiResponse(description="Delete an city")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable final Long id,
                                             @RequestHeader(value = HttpHeaders.IF_MATCH) final Long version) {

        return cityService.deleteById(id, version)
                   .map(empty -> noContent().build());
    }

    @GetMapping("/events")
    public Flux<ServerSentEvent<Event>> listenToEvents() {

        final Flux<Event> citySavedFlux =
                this.cityService.listenToSavedCities()
                        .map(cityMapper::toResource)
                        .map(CitySaved::new);

        final Flux<Event> cityDeletedFlux =
                this.cityService.listenToDeletedCities()
                        .map(CityDeleted::new);

        return Flux.merge(citySavedFlux, cityDeletedFlux)
                .map(event -> ServerSentEvent.<Event>builder()
                        .retry(Duration.ofSeconds(4L))
                        .event(event.getClass().getSimpleName())
                        .data(event).build());
    }

}
