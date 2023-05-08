package com.weather.weatherapp.repository;

import com.weather.weatherapp.model.CityPerson;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CityPersonRepository extends ReactiveCrudRepository<CityPerson, Long>, ReactiveSortingRepository<CityPerson, Long> {

    Flux<CityPerson> findAllByCityId(Long cityId);

    Mono<Integer> deleteAllByCityId(Long cityId);

}
