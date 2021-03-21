package com.example.core.dto.open_forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ForecastDto implements Serializable {

    @JsonProperty("main")
    private MainValueDto value;

    public MainValueDto getValue() {
        return value;
    }

    public void setValue(MainValueDto value) {
        this.value = value;
    }

}
