package com.weather.weatherapp.service;

import com.weather.weatherapp.exception.CityNotFoundException;
import com.weather.weatherapp.exception.UnexpectedCityVersionException;
import com.weather.weatherapp.mapper.PersonMapper;
import com.weather.weatherapp.model.City;
import com.weather.weatherapp.model.CityPerson;
import com.weather.weatherapp.repository.CityPersonRepository;
import com.weather.weatherapp.repository.CityRepository;
import com.weather.weatherapp.repository.PersonRepository;
import com.weather.weatherapp.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.weather.weatherapp.model.NotificationTopic.CITY_DELETED;
import static com.weather.weatherapp.model.NotificationTopic.CITY_SAVED;

@Service
@RequiredArgsConstructor
public class CityService {

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Order.by("lastModifiedDate"));

    private final NotificationService notificationService;

    private final CityRepository cityRepository;
    private final WeatherRepository weatherRepository;
    private final CityPersonRepository cityPersonRepository;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    /**
     * Find all cities
     * @return Find all cities with the related objects loaded
     */
    public Flux<City> findAll() {
        return cityRepository.findAll(DEFAULT_SORT)
                .flatMap(this::loadRelations);
    }

    /**
     * Create a new city
     * @param city City to be created
     *
     * @return the saved city without the related entities
     */
    @Transactional
    public Mono<City> create(City city) {

        if(city.getId() != null || city.getVersion() != null) {
            return Mono.error(new IllegalArgumentException("When creating an city, the id and the version must be null"));
        }

        return  // Save the new city
                cityRepository.save(city)
                // Save the links to the weathers
                .flatMap(savedCity ->
                     cityPersonRepository.saveAll(personMapper.toCityPersons(savedCity.getId(), savedCity.getPersons()))
                         .collectList()
                         // Return the newly created city
                         .then(Mono.just(savedCity)));
    }

    /**
     * Update an City
     * @param cityToSave city to be saved
     * @return the saved city without the related entities
     */
    @Transactional
    public Mono<City> update(City cityToSave) {

        if(cityToSave.getId() == null || cityToSave.getVersion() == null) {
            return Mono.error(new IllegalArgumentException("When updating an city, the id and the version must be provided"));
        }

        return verifyExistence(cityToSave.getId())

                // Find the existing link to the weathers
               .then(cityPersonRepository.findAllByCityId(cityToSave.getId()).collectList())

               // Remove and add the links to the weathers
               .flatMap(currentCityPersons -> {

                   // As R2DBC does not support embedded IDs, the CityPerson entity has a technical key
                   // We can't just replace all CitiePersons, we need to generate the proper insert/delete statements

                   final Collection<Long> existingPersonIds = personMapper.extractPersonIdsFromCityPersons(currentCityPersons);
                   final Collection<Long> personIdsToSave = personMapper.extractPersonIdsFromPersons(cityToSave.getPersons());

                   // City Persons to be deleted
                   final Collection<CityPerson> removedCityPersons = currentCityPersons.stream()
                           .filter(cityPerson ->  personIdsToSave.contains(cityPerson.getPersonId()))
                           .collect(Collectors.toList());

                   // City Persons to be inserted
                   final Collection<CityPerson> addedCityPersons = personIdsToSave.stream()
                           .filter(personId -> !existingPersonIds.contains(personId))
                           .map(personId -> new CityPerson(cityToSave.getId(), personId))
                           .collect(Collectors.toList());

                   return cityPersonRepository.deleteAll(removedCityPersons)
                           .then(cityPersonRepository.saveAll(addedCityPersons).collectList());
               })

               // Save the city
               .then(cityRepository.save(cityToSave));
    }

    @Transactional
    public Mono<Void> deleteById(final Long id, final Long version) {

        return  // Find the city to delete
                findById(id, version, false)

                // Delete the links to the weathers
                .zipWith(cityPersonRepository.deleteAllByCityId(id))

                // Return the city
                .map(Tuple2::getT1)

                // Delete the city
                .flatMap(cityRepository::delete);
    }

    /**
     * Find an city
     *
     * @param id            identifier of the city
     * @param version       expected version to be retrieved
     * @param loadRelations true if the related objects must also be retrieved
     *
     * @return the city
     *
     */
    public Mono<City> findById(final Long id, final Long version, final boolean loadRelations) {

        final Mono<City> cityMono = cityRepository.findById(id)
                .switchIfEmpty(Mono.error(new CityNotFoundException(id)))
                .handle((city, sink) -> {
                    // Optimistic locking: pre-check
                    if (version != null && !version.equals(city.getVersion())) {
                        // The version are different, return an error
                        sink.error(new UnexpectedCityVersionException(version, city.getVersion()));
                    } else {
                        sink.next(city);
                    }
                });

        // Load the related objects, if requested
        return loadRelations ? cityMono.flatMap(this::loadRelations) : cityMono;
    }

    /**
     * Listen to all saved cities
     *
     * @return the saved cities
     */
    public Flux<City> listenToSavedCities() {

        return this.notificationService.listen(CITY_SAVED, City.class)
                .flatMap(this::loadRelations);
    }

    /**
     * Listen to all deleted cities
     *
     * @return the ID of the deleted cities
     */
    public Flux<Long> listenToDeletedCities() {

        return this.notificationService.listen(CITY_DELETED, City.class)
                .map(City::getId);
    }

    /**
     * Load the objects related to an city
     * @param city City
     * @return The cities with the loaded related objects (weather, persons)
     */
    private Mono<City> loadRelations(final City city) {

        // Load the weathers
        Mono<City> mono = Mono.just(city)
                .zipWith(personRepository.findPersonsByCityId(city.getId()).collectList())
                .map(result -> result.getT1().setPersons(result.getT2()));

        // Load the weather (if set)
        if (city.getWeatherId() != null) {
            mono = mono.zipWith(weatherRepository.findById(city.getWeatherId()))
                    .map(result -> result.getT1().setWeather(result.getT2()));
        }

        return mono;
    }

    private Mono<Boolean> verifyExistence(Long id) {
        return cityRepository.existsById(id).handle((exists, sink) -> {
            if (Boolean.FALSE.equals(exists)) {
                sink.error(new CityNotFoundException(id));
            } else {
                sink.next(exists);
            }
        });
    }

}
