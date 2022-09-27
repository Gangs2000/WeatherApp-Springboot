package com.weatherapp.WeatherDesignService.FeignClientInterface;

import com.weatherapp.WeatherDesignService.Model.CityData;
import com.weatherapp.WeatherDesignService.Model.WeatherForecast;
import com.weatherapp.WeatherDesignService.Model.WeatherInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "feignClient", url = "localhost:9090/city")
public interface FeignClientResponseInterface {
    @GetMapping("/get-city/city/{cityName}")
    CityData getCityData(@PathVariable("cityName") String cityName);

    @GetMapping("/get-current-weather/{cityName}")
    WeatherInfo getCurrentWeather(@PathVariable("cityName") String cityName);

    @GetMapping("/week-forecast/{cityName}")
    WeatherForecast getWeatherForeCast(@PathVariable("cityName") String cityName);
}
