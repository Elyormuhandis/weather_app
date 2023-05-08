package com.weather.weatherapp.repository;

import com.weather.weatherapp.model.City;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends ReactiveCrudRepository<City, Long>, ReactiveSortingRepository<City, Long> {


}
