package com.weather.weatherapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class UnexpectedCityVersionException extends NotFoundException {

    public UnexpectedCityVersionException(Long expectedVersion, Long foundVersion) {
        super(String.format("The city has a different version than the expected one. Expected [%s], found [%s]",
                expectedVersion, foundVersion));
    }

}
