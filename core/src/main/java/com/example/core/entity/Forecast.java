package com.example.core.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Forecast")
public class Forecast implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String city;

    @OneToMany(mappedBy = "forecast", cascade = CascadeType.ALL)
    @OrderBy("date asc")
    private List<ForecastItem> forecastItemList;

    public Forecast() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<ForecastItem> getForecastItemList() {
        return forecastItemList;
    }

    public void setForecastItemList(List<ForecastItem> forecastItemList) {
        this.forecastItemList = forecastItemList;
    }
}
