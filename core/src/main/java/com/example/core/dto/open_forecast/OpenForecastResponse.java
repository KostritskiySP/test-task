package com.example.core.dto.open_forecast;

import com.example.core.utils.MillisOrLocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
