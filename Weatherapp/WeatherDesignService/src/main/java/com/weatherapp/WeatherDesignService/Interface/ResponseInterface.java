package com.weatherapp.WeatherDesignService.Interface;

import com.weatherapp.WeatherDesignService.Model.CityData;
import com.weatherapp.WeatherDesignService.Model.WeatherForecast;
import com.weatherapp.WeatherDesignService.Model.WeatherInfo;
import org.springframework.ui.ModelMap;

public interface ResponseInterface {
    void updatePIN(String emailId);
    void resetPIN(String emailId, String password);
    CityData getCityData(String cityName);
    WeatherInfo getCurrentWeather(String cityName);
    WeatherForecast getWeatherForeCast(String cityName);
    void setModelAttributes(ModelMap modelMap);
}
