package com.example.core.service;

import com.example.core.component.ForecastExtenderComponent;
import com.example.core.dto.open_forecast.OpenForecastResponse;
import com.example.core.entity.Forecast;
import com.example.core.entity.ForecastItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

@Service
public class WeatherForecastService {

    private static final String FORECAST_PATH = "forecast";

    @Autowired
    private ForecastExtenderComponent forecastExtenderComponent;
    @Autowired
    private ForecastService forecastService;

    @Value("${open-forecast.APIkey}")
    private String apiKey;
    @Value("${open-forecast.targetURL}")
    private String targetURL;

    public Forecast getForecast(String cityName) {
        Forecast cache = forecastService.loadCacheValues(cityName);
        if (cache != null) {
            return cache;
        }
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("q", cityName);
        requestParams.add("appid", apiKey);
        OpenForecastResponse response = sendRequest(requestParams);
        if (CollectionUtils.isEmpty(response.getForecastItemList())) {
            return null;
        }
        Forecast forecast = new Forecast();
        forecast.setCity(cityName);
        forecast.setForecastItemList(response.getForecastItemList()
                .stream()
                .map(forecastItemDto -> {
                    ForecastItem item = new ForecastItem();
                    item.setForecast(forecast);
                    item.setTemperature(forecastItemDto.getValue().getTemperature());
                    item.setDate(forecastItemDto.getDate());
                    return item;
                })
                .collect(Collectors.toList())
        );
        forecastExtenderComponent.extend(forecast);
        forecastService.updateForecast(forecast);
        return forecast;
    }

    private OpenForecastResponse sendRequest(MultiValueMap<String, String> params) {

        URI requestUri = UriComponentsBuilder.fromUriString(targetURL + FORECAST_PATH)
                .queryParams(params)
                .build()
                .encode()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<OpenForecastResponse> responseEntity;
        responseEntity = restTemplate.exchange(requestUri, HttpMethod.GET, null, OpenForecastResponse.class);

        return responseEntity.getBody();
    }
}
