package com.example.core.dto.open_forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class OpenForecastResponse implements Serializable {

    @JsonProperty("list")
    private List<ForecastDto> forecastList;

    public List<ForecastDto> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<ForecastDto> forecastList) {
        this.forecastList = forecastList;
    }
}
