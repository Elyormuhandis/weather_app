package com.weather.weatherapp.service;

import com.weather.weatherapp.exception.CityNotFoundException;
import com.weather.weatherapp.model.Weather;
import com.weather.weatherapp.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherService {

    // Note that the name of the fields to be sorted on are the DB field names
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Order.by("first_name,last_name"));

    private final WeatherRepository weatherRepository;

    public Flux<Weather> findAll() {
        return weatherRepository.findAll(DEFAULT_SORT);
    }

    /**
     * Find a person
     *
     * @param id      identifier of the person
     * @return the person
     */
    public Mono<Weather> findById(final Long id) {

        return weatherRepository.findById(id)
                .switchIfEmpty(Mono.error(new CityNotFoundException(id)));
    }


}
