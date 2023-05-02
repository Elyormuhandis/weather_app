package com.weather.weatherapp.city;

import com.weather.weatherapp.current_weather.CurrentWeather;
import com.weather.weatherapp.current_weather.CurrentWeatherRepository;
import com.weather.weatherapp.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CurrentWeatherRepository currentWeatherRepository;

    public CityService(final CityRepository cityRepository,
            final CurrentWeatherRepository currentWeatherRepository) {
        this.cityRepository = cityRepository;
        this.currentWeatherRepository = currentWeatherRepository;
    }

    public List<CityDTO> findAll() {
        final List<City> citys = cityRepository.findAll(Sort.by("id"));
        return citys.stream()
                .map((city) -> mapToDTO(city, new CityDTO()))
                .toList();
    }

    public CityDTO get(final Long id) {
        return cityRepository.findById(id)
                .map(city -> mapToDTO(city, new CityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CityDTO cityDTO) {
        final City city = new City();
        mapToEntity(cityDTO, city);
        return cityRepository.save(city).getId();
    }

    public void update(final Long id, final CityDTO cityDTO) {
        final City city = cityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cityDTO, city);
        cityRepository.save(city);
    }

    public void delete(final Long id) {
        cityRepository.deleteById(id);
    }

    private CityDTO mapToDTO(final City city, final CityDTO cityDTO) {
        cityDTO.setId(city.getId());
        cityDTO.setName(city.getName());
        cityDTO.setCurrentWeather(city.getCurrentWeather() == null ? null : city.getCurrentWeather().getId());
        return cityDTO;
    }

    private City mapToEntity(final CityDTO cityDTO, final City city) {
        city.setName(cityDTO.getName());
        final CurrentWeather currentWeather = cityDTO.getCurrentWeather() == null ? null : currentWeatherRepository.findById(cityDTO.getCurrentWeather())
                .orElseThrow(() -> new NotFoundException("currentWeather not found"));
        city.setCurrentWeather(currentWeather);
        return city;
    }

    public boolean nameExists(final String name) {
        return cityRepository.existsByNameIgnoreCase(name);
    }

}
