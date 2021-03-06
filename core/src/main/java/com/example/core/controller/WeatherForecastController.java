package com.example.core.controller;

import com.example.core.dto.ForecastResponse;
import com.example.core.service.WeatherForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path = "api/weather")
public class WeatherForecastController {

    @Autowired
    private WeatherForecastService weatherForecastService;

    @GetMapping
    public ForecastResponse getForecast(@RequestParam String city) {
        return new ForecastResponse(weatherForecastService.getForecast(city));
    }
}
