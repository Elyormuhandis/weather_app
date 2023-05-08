package com.weather.weatherapp.repository;

import com.weather.weatherapp.model.Weather;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends ReactiveCrudRepository<Weather, Long>, ReactiveSortingRepository<Weather, Long> {


}
