package com.example.core.dto.open_forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class OpenForecastResponse implements Serializable {

    @JsonProperty("list")
    private List<ForecastItemDto> forecastItemList;

    public List<ForecastItemDto> getForecastItemList() {
        return forecastItemList;
    }

    public void setForecastItemList(List<ForecastItemDto> forecastItemList) {
        this.forecastItemList = forecastItemList;
    }
}
