package com.weather.weatherapp.rest.api;

import com.weather.weatherapp.model.enums.Role;
import lombok.Data;

@Data
public class PersonResource {

    private Long id;
    private String fullName;
    private String username;
    private String password;
    private Role role;
    
}
