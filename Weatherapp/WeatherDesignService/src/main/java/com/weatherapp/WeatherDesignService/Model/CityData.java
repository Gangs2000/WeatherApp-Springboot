package com.weatherapp.WeatherDesignService.Model;

import lombok.*;
import org.springframework.stereotype.Component;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class CityData implements Serializable {
    private long cityId;
    private String cityName;
    private String country;
    private long latitude;
    private long longitude;
    private long population;
    private long timeZone;
    private Object sunRise;
    private Object sunSet;
}
