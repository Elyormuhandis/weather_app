package com.weather.weatherapp.current_weather;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CurrentWeatherRepository extends JpaRepository<CurrentWeather, Long> {
}
