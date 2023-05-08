package com.weather.weatherapp.mapper;

import com.weather.weatherapp.model.CityPerson;
import com.weather.weatherapp.model.Person;
import com.weather.weatherapp.rest.api.PersonResource;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;



@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonResource toResource(Person person);

    default List<Person> toPersons(Collection<Long> personsId) {

        if (personsId == null) {
            return new ArrayList<>();
        }

        // Build persons containing only the ID
        return personsId.stream()
                .map(personId -> new Person().setId(personId))
                .collect(Collectors.toList());
    }

    default Collection<Long> extractPersonIdsFromPersons(Collection<Person> persons) {
        if (persons == null) {
            return new LinkedHashSet<>();
        }

        return persons.stream().map(Person::getId).collect(Collectors.toSet());
    }

    default Collection<Long> extractPersonIdsFromCityPersons(Collection<CityPerson> cityPersons) {
        if (cityPersons == null) {
            return new LinkedHashSet<>();
        }

        return cityPersons.stream().map(CityPerson::getPersonId).collect(Collectors.toSet());
    }


    default Collection<CityPerson> toCityPersons(Long cityId, Collection<Person> persons) {
        if (persons == null) {
            return new LinkedHashSet<>();
        }

        return persons.stream()
                .map(person -> new CityPerson(cityId, person.getId()))
                .collect(Collectors.toSet());
    }

}
