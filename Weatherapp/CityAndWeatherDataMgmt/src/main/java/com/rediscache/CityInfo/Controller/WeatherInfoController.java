package com.rediscache.CityInfo.Controller;

import com.rediscache.CityInfo.Model.WeatherForecast;
import com.rediscache.CityInfo.Model.WeatherInfo;
import com.rediscache.CityInfo.Service.WeatherInfoService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city")
public class WeatherInfoController {

    @Autowired WeatherInfoService weatherInfoService;

    @GetMapping("/get-current-weather/{cityName}")
    public ResponseEntity<WeatherInfo> getCurrentWeather(@PathVariable("cityName") String cityName) throws JSONException {
        return new ResponseEntity<>(weatherInfoService.getCurrentWeather(cityName), HttpStatus.ACCEPTED);
    }

    @GetMapping("/week-forecast/{cityName}")
    public ResponseEntity<WeatherForecast> weekForecast(@PathVariable("cityName") String cityName) throws JSONException {
        return new ResponseEntity<>(weatherInfoService.weekForecast(cityName),HttpStatus.ACCEPTED);
    }
}
