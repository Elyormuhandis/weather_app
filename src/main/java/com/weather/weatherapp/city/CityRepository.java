package com.weather.weatherapp.city;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CityRepository extends JpaRepository<City, Long> {

    boolean existsByNameIgnoreCase(String name);

}
