package com.weather.weatherapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Table
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class City {

    public City(Long id, Long version) {
        this.id = id;
        this.version = version;
    }

    @Id
    private Long id;

    @Version
    private Long version;

    @Size(max=4000)
    @NotBlank
    private String name;

    @NotNull
    private Long weatherId;

    @NotNull
    private Boolean enabled = true;

    @Transient
    private Weather weather;

    @Transient
    private List<Person> persons;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
