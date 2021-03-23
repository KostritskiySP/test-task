package com.example.core.dto.open_forecast;

import com.example.core.utils.MillisOrLocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ForecastItemDto implements Serializable {

    @JsonProperty("main")
    private MainValueDto value;

    @JsonProperty("dt")
    @JsonDeserialize(using = MillisOrLocalDateTimeDeserializer.class)
    private LocalDateTime date;

    public MainValueDto getValue() {
        return value;
    }

    public void setValue(MainValueDto value) {
        this.value = value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
