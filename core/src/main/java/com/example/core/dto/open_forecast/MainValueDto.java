package com.example.core.dto.open_forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MainValueDto implements Serializable {

    @JsonProperty("temp")
    private Double temperature;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
