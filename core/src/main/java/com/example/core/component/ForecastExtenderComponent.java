package com.example.core.component;

import com.example.core.dto.open_forecast.ForecastDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ForecastExtenderComponent {

    private static final int INDEX_RANGE = 3;

    public List<Double> extend(List<ForecastDto> forecastList) {
        if (CollectionUtils.isEmpty(forecastList)) {
            return Collections.emptyList();
        }
        CircularArrayList<ForecastDto> circularForecastList = new CircularArrayList<>(forecastList);
        CircularArrayList<Double> result = forecastList.stream()
                .map(forecast -> forecast.getValue().getTemperature())
                .collect(Collectors.toCollection(CircularArrayList::new));
        Function<Integer, Double> getValueFunction = (index) -> circularForecastList.get(index).getValue().getTemperature();
        int size = result.size();
        for (int indexToPredict = size - 1; indexToPredict < size + 40; indexToPredict++) {
            result.add(makePrediction(INDEX_RANGE, getValueFunction, indexToPredict));
        }
        return result;
    }

    public static class CircularArrayList<E> extends ArrayList<E> {

        public CircularArrayList(List<E> forecastList) {
            super(forecastList);
        }

        public CircularArrayList() {
        }

        @Override
        public E get(int index) {
            if (index < 0) {
                index = size() - -index % size();
            } else if (index >= size()) {
                index = index % size();
            }

            return super.get(index);
        }
    }

    private double makePrediction(int indexRange, Function<Integer, Double> getValueFunction, int indexToPredict) {
        if (indexRange == 0) {
            return getValueFunction.apply(indexToPredict);
        }
        double left = 0;
        for (int k = 1; k < indexRange + 1; k++) {
            left += getValueFunction.apply(-k);
        }
        left = (left / 3) * (1 - indexToPredict * 0.02);
        double right = 0;
        for (int k = 1; k < indexRange + 1; k++) {
            right += getValueFunction.apply(k);
        }
        right = (right / 3) * (indexToPredict * 0.02);
        return (getValueFunction.apply(indexToPredict) + left + right) / 2;
    }
}
