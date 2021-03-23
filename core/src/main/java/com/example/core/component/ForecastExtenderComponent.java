package com.example.core.component;

import com.example.core.entity.Forecast;
import com.example.core.entity.ForecastItem;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

@Component
public class ForecastExtenderComponent {

    private static final int INDEX_RANGE = 3;
    private static final int ADDITIONAL_STEPS_NUMBER = 40;
    private static final int HOURS_IN_STEP = 3;

    public void extend(Forecast forecast) {
        List<ForecastItem> forecastItemList = forecast.getForecastItemList();
        if (CollectionUtils.isEmpty(forecastItemList)) {
            return;
        }
        CircularArrayList<ForecastItem> circularForecastList = new CircularArrayList<>(forecastItemList);
        CircularArrayList<ForecastItem> result = new CircularArrayList<>(forecastItemList);
        Function<Integer, Double> getValueFunction = (index) -> circularForecastList.get(index).getTemperature();
        int size = result.size();
        LocalDateTime date = circularForecastList.get(-1).getDate();
        for (int indexToPredict = size; indexToPredict < size + ADDITIONAL_STEPS_NUMBER; indexToPredict++) {
            ForecastItem forecastItem = new ForecastItem();
            forecastItem.setForecast(forecast);
            date = date.plusHours(HOURS_IN_STEP);
            forecastItem.setDate(date);
            forecastItem.setTemperature(makePrediction(INDEX_RANGE, getValueFunction, indexToPredict));
            result.add(forecastItem);
        }
        forecast.setForecastItemList(new ArrayList<>(result));
    }

    private Date addHoursToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, HOURS_IN_STEP);
        return calendar.getTime();
    }

    public static class CircularArrayList<E> extends ArrayList<E> {

        public CircularArrayList(List<E> forecastList) {
            super(forecastList);
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
        for (int offset = 1; offset < indexRange + 1; offset++) {
            left += getValueFunction.apply(-offset);
        }
        left = (left / 3) * (1 - indexToPredict * 0.02);
        double right = 0;
        for (int offset = 1; offset < indexRange + 1; offset++) {
            right += getValueFunction.apply(offset);
        }
        right = (right / 3) * (indexToPredict * 0.02);
        return (getValueFunction.apply(indexToPredict) + left + right) / 2;
    }
}
