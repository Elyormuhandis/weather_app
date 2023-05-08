package com.weather.weatherapp.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CityPerson {

   public CityPerson(Long cityId, Long personId) {
      this.cityId = cityId;
      this.personId = personId;
   }

   @Id
   private Long id;

   @NotNull
   private Long cityId;

   @NotNull
   private Long personId;

}
