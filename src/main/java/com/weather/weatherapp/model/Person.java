package com.weather.weatherapp.model;

import com.weather.weatherapp.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Person {

    @Id
    private Long id;

    @Version
    private Long version;

    @NotBlank
    @Size(max=64)
    private String fullName;

    @NotBlank
    @Size(min=3)
    private String username;

    @NotBlank
    @Size(min=3)
    private String password;

    @NotBlank
    private Role role;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
