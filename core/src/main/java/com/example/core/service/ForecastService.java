package com.example.core.service;

import com.example.core.entity.Forecast;
import com.example.core.repository.ForecastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class ForecastService {

    @Autowired
    private ForecastRepository forecastRepository;

    public void updateForecast(Forecast forecast) {
        forecastRepository.deleteByCity(forecast.getCity());
        forecastRepository.save(forecast);
    }

    public Forecast loadCacheValues(String cityName) {
        Forecast forecast = forecastRepository.findByCity(cityName);
        if (forecast == null) {
            return null;
        }
        if (!CollectionUtils.isEmpty(forecast.getForecastItemList())) {
            if (forecast.getForecastItemList().get(0).getDate().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
                return null;
            }
        }
        return forecast;
    }
}
