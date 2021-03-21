package com.example.core.controller;

import com.example.core.service.WeatherForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/weather")
public class WeatherForecastController {

    @Autowired
    private WeatherForecastService weatherForecastService;

    @GetMapping
    public List<Double> getForecast(@RequestParam String cityName) {
        return weatherForecastService.getForecast(cityName);
    }
}
