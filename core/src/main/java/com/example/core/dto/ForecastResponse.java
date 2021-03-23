package com.example.core.dto;

import com.example.core.entity.Forecast;
import com.example.core.entity.ForecastItem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class ForecastResponse implements Serializable {

    private Map<LocalDateTime, Double> forecast;

    public ForecastResponse() {
    }

    public ForecastResponse(Forecast forecast) {
        this.forecast = forecast != null
                ? forecast.getForecastItemList().stream()
                .collect(Collectors.toMap(ForecastItem::getDate, ForecastItem::getTemperature))
                : null;
    }

    public Map<LocalDateTime, Double> getForecast() {
        return forecast;
    }

    public void setForecast(Map<LocalDateTime, Double> forecast) {
        this.forecast = forecast;
    }
}
