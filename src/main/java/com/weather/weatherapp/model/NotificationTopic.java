package com.weather.weatherapp.model;

import lombok.Getter;

/**
 * PostgreSQL notification topics
 */
@Getter
public enum NotificationTopic {

    CITY_SAVED,
    CITY_DELETED
}
