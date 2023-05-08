package com.weather.weatherapp.repository;

import com.weather.weatherapp.model.Person;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PersonRepository extends ReactiveCrudRepository<Person, Long>, ReactiveSortingRepository<Person, Long> {

    @Query("select p.* from person p join city_person cp on p.id = cp.person_id where cp.city_id = :city_id order by p.name")
    Flux<Person> findPersonsByCityId(Long cityId);

}
