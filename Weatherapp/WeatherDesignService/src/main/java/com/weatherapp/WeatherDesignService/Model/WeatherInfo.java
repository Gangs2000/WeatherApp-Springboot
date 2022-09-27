package com.weatherapp.WeatherDesignService.Model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class WeatherInfo implements Serializable {
    private String cityName;
    private String currentWeather;
    private String weatherDescription;
    private double temperature;
    private double tempMin;
    private double tempMax;
    private double celsius;
    private double fahrenheit;
    private double windSpeed;
    private LocalDateTime lastUpdated;
    private String icon;
}
