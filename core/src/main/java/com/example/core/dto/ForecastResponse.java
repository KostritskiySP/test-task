package com.example.core.dto;

import com.example.core.entity.Forecast;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ForecastResponse implements Serializable {

    private List<ForecastDataItemDto> forecast;

    public ForecastResponse() {
    }

    public ForecastResponse(Forecast forecast) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        this.forecast = forecast != null
                ? forecast.getForecastItemList().stream()
                .map(forecastItem -> new ForecastDataItemDto(forecastItem.getDate().format(dateTimeFormatter), forecastItem.getTemperature()))
                .collect(Collectors.toList())
                : null;
    }

    public List<ForecastDataItemDto> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastDataItemDto> forecast) {
        this.forecast = forecast;
    }
}
