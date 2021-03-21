package com.example.core.service;

import com.example.core.component.ForecastExtenderComponent;
import com.example.core.dto.open_forecast.OpenForecastResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class WeatherForecastService {

    private static final String FORECAST_PATH = "forecast";

    @Autowired
    private ForecastExtenderComponent forecastExtenderComponent;

    @Value("${open-forecast.APIkey}")
    private String apiKey;
    @Value("${open-forecast.targetURL}")
    private String targetURL;

    public List<Double> getForecast(String cityName) {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("q", cityName);
        requestParams.add("appid", apiKey);
        OpenForecastResponse response = sendRequest(requestParams);
        return forecastExtenderComponent.extend(response.getForecastList());
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
        } catch (ResourceAccessException e) {
//            throw new ProviderNotAvailableException(); //todo
            throw new RuntimeException();
        }

        OpenForecastResponse response = responseEntity.getBody();
        HttpStatus responseStatus = responseEntity.getStatusCode();

        if (!responseStatus.is2xxSuccessful()) {

            if (responseStatus.value() == HttpStatus.UNAUTHORIZED.value()) {
//                throw new AuthorizationErrorException();
                //todo
                throw new RuntimeException();
            }
//            throw new ProviderSideException();
            //todo
            throw new RuntimeException();
        }

        return response;
    }
}
