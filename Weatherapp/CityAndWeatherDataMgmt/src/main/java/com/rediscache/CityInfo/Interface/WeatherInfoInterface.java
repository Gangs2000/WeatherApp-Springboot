package com.rediscache.CityInfo.Interface;

import com.rediscache.CityInfo.Model.WeatherForecast;
import com.rediscache.CityInfo.Model.WeatherInfo;
import org.json.JSONException;

public interface WeatherInfoInterface {
    WeatherInfo getCurrentWeather(String cityName) throws JSONException;
    WeatherInfo cacheCityWeather(String cityName) throws JSONException;
    WeatherInfo fetchCurrentWeatherFromOpenWeather(String cityName) throws JSONException;
    void updateCurrentWeather(String cityName);
    WeatherForecast weekForecast(String cityName) throws JSONException;
    WeatherForecast cacheCityWeatherForecast(String cityName) throws JSONException;
    WeatherForecast fetchWeatherForecastFormOpenWeather(String cityName) throws JSONException;
    void updateWeatherForecast(String cityName);
}
