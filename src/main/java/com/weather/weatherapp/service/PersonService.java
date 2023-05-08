package com.weather.weatherapp.service;

import com.weather.weatherapp.exception.WeatherNotFoundException;
import com.weather.weatherapp.model.Person;
import com.weather.weatherapp.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PersonService {

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Order.by("name"));

    private final PersonRepository personRepository;

    public Flux<Person> findAll() {
        return personRepository.findAll(DEFAULT_SORT);
    }

    /**
     * Find a Person
     *
     * @param id      identifier of the person
     * @return the person
     */
    public Mono<Person> findById(final Long id) {

        return personRepository.findById(id)
                .switchIfEmpty(Mono.error(new WeatherNotFoundException(id)));
    }


}
