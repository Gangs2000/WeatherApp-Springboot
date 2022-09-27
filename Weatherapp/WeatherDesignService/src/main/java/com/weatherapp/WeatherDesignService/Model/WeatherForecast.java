package com.weatherapp.WeatherDesignService.Model;

import lombok.*;

import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class WeatherForecast implements Serializable {
    private String _id;
    private String cityName;
    private List<ForeCastForWeek> foreCastForWeekList;
    private LocalDateTime lastUpdated;
}
