package com.weather.weatherapp.mapper;

import com.weather.weatherapp.model.City;
import com.weather.weatherapp.rest.api.CityPatchResource;
import com.weather.weatherapp.rest.api.CityResource;
import com.weather.weatherapp.rest.api.CityUpdateResource;
import com.weather.weatherapp.rest.api.NewCityResource;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {WeatherMapper.class, PersonMapper.class})
public abstract class CityMapper {

    @Autowired
    private PersonMapper personMapper;

    public abstract CityResource toResource(City city);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "weather", ignore = true)
    @Mapping(target = "persons", ignore = true)
    public abstract City toModel(NewCityResource cityResource);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "weather", ignore = true)
    @Mapping(target = "persons", ignore = true)
    public abstract City update(CityUpdateResource cityResource, @MappingTarget City city);

    @AfterMapping
    public void afterMapping(NewCityResource cityResource, @MappingTarget City city) {
        city.setPersons(personMapper.toPersons(cityResource.getPersonIds()));
    }

    @AfterMapping
    public void afterMapping(CityUpdateResource cityResource, @MappingTarget City city) {
        city.setPersons(personMapper.toPersons(cityResource.getPersonIds()));
    }

    @SuppressWarnings({"OptionalAssignedToNull"})
    public City patch(CityPatchResource patchResource, City city) {
        if (patchResource.getDescription() != null) {
            // The description has been provided in the patch
            city.setName(patchResource.getDescription().orElse(null));
        }

        if (patchResource.getEnabled() != null) {
            // The enabled has been provided in the patch
            city.setEnabled(patchResource.getEnabled().orElse(null));
        }

        if(patchResource.getWeatherId() != null) {
            city.setWeatherId(patchResource.getWeatherId().orElse(null));
        }

        if(patchResource.getPersonIds() != null) {
            // Set persons objects containing only the ID
            city.setPersons(personMapper.toPersons(patchResource.getPersonIds().orElse(null)));
        }
        return city;
    }

}
