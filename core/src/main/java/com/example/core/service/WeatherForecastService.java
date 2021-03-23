package com.example.core.service;

import com.example.core.component.ForecastExtenderComponent;
import com.example.core.dto.open_forecast.OpenForecastResponse;
import com.example.core.entity.Forecast;
import com.example.core.entity.ForecastItem;
import com.example.core.validation.BadRequestException;
import com.example.core.validation.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
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

    public Forecast getForecast(String city) {
        if (!StringUtils.hasText(city)) {
            throw new BadRequestException();
        }
        Forecast cache = forecastService.loadCacheValues(city);
        if (cache != null) {
            return cache;
        }
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("q", city);
        requestParams.add("units", "metric");
        requestParams.add("appid", apiKey);
        OpenForecastResponse response = sendRequest(requestParams);
        if (CollectionUtils.isEmpty(response.getForecastItemList())) {
            return null;
        }
        Forecast forecast = new Forecast();
        forecast.setCity(city);
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
        try {
            responseEntity = restTemplate.exchange(requestUri, HttpMethod.GET, null, OpenForecastResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new NotFoundException();
            }
            throw e;
        }

        return responseEntity.getBody();
    }
}
