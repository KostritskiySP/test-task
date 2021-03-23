package com.example.core.dto;

import java.io.Serializable;

class ForecastDataItemDto implements Serializable {

    private String name;

    private Double value;

    public ForecastDataItemDto(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
