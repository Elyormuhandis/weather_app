package com.weather.weatherapp.exception;

import com.weather.weatherapp.model.NotificationTopic;

public class NotificationDeserializationException extends RuntimeException {

    public NotificationDeserializationException(NotificationTopic topic, Throwable cause) {
        super(String.format("Cannot deserialize the notification for topic [%s]", topic), cause);
    }

}
