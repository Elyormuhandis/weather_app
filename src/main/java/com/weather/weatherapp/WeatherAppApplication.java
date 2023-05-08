package com.weather.weatherapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.stream.IntStream;

@SpringBootApplication
@OpenAPIDefinition
@EnableR2dbcAuditing
public class WeatherAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherAppApplication.class, args);
    }


}
