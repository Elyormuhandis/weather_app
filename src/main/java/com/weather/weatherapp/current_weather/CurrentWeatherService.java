package com.weather.weatherapp.current_weather;

import com.weather.weatherapp.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CurrentWeatherService {

    private final CurrentWeatherRepository currentWeatherRepository;

    public CurrentWeatherService(final CurrentWeatherRepository currentWeatherRepository) {
        this.currentWeatherRepository = currentWeatherRepository;
    }

    public List<CurrentWeatherDTO> findAll() {
        final List<CurrentWeather> currentWeathers = currentWeatherRepository.findAll(Sort.by("id"));
        return currentWeathers.stream()
                .map((currentWeather) -> mapToDTO(currentWeather, new CurrentWeatherDTO()))
                .toList();
    }

    public CurrentWeatherDTO get(final Long id) {
        return currentWeatherRepository.findById(id)
                .map(currentWeather -> mapToDTO(currentWeather, new CurrentWeatherDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CurrentWeatherDTO currentWeatherDTO) {
        final CurrentWeather currentWeather = new CurrentWeather();
        mapToEntity(currentWeatherDTO, currentWeather);
        return currentWeatherRepository.save(currentWeather).getId();
    }

    public void update(final Long id, final CurrentWeatherDTO currentWeatherDTO) {
        final CurrentWeather currentWeather = currentWeatherRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(currentWeatherDTO, currentWeather);
        currentWeatherRepository.save(currentWeather);
    }

    public void delete(final Long id) {
        currentWeatherRepository.deleteById(id);
    }

    private CurrentWeatherDTO mapToDTO(final CurrentWeather currentWeather,
            final CurrentWeatherDTO currentWeatherDTO) {
        currentWeatherDTO.setId(currentWeather.getId());
        currentWeatherDTO.setMain(currentWeather.getMain());
        currentWeatherDTO.setDescription(currentWeather.getDescription());
        currentWeatherDTO.setTemperature(currentWeather.getTemperature());
        currentWeatherDTO.setVisibility(currentWeather.getVisibility());
        currentWeatherDTO.setPressure(currentWeather.getPressure());
        currentWeatherDTO.setHumidity(currentWeather.getHumidity());
        currentWeatherDTO.setWindSpeed(currentWeather.getWindSpeed());
        currentWeatherDTO.setWindDegree(currentWeather.getWindDegree());
        return currentWeatherDTO;
    }

    private CurrentWeather mapToEntity(final CurrentWeatherDTO currentWeatherDTO,
            final CurrentWeather currentWeather) {
        currentWeather.setMain(currentWeatherDTO.getMain());
        currentWeather.setDescription(currentWeatherDTO.getDescription());
        currentWeather.setTemperature(currentWeatherDTO.getTemperature());
        currentWeather.setVisibility(currentWeatherDTO.getVisibility());
        currentWeather.setPressure(currentWeatherDTO.getPressure());
        currentWeather.setHumidity(currentWeatherDTO.getHumidity());
        currentWeather.setWindSpeed(currentWeatherDTO.getWindSpeed());
        currentWeather.setWindDegree(currentWeatherDTO.getWindDegree());
        return currentWeather;
    }

}
