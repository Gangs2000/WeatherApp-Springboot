package com.rediscache.CityInfo.Service;

import com.rediscache.CityInfo.Interface.WeatherInfoInterface;
import com.rediscache.CityInfo.Model.ForeCastForWeek;
import com.rediscache.CityInfo.Model.WeatherForecast;
import com.rediscache.CityInfo.Model.WeatherInfo;
import com.rediscache.CityInfo.Repository.WeatherForecastRepository;
import com.rediscache.CityInfo.Repository.WeatherInfoRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class WeatherInfoService implements WeatherInfoInterface {

    private final String apiKey="94043c06b6556c7b17378841ceded534";

    @Autowired WeatherInfoInterface weatherInfoInterface;
    DecimalFormat decimalFormat = new DecimalFormat("#.00");
    @Autowired
    WeatherInfo weatherInfo;
    @Autowired
    WeatherInfoRepository weatherInfoRepository;
    @Autowired
    WeatherForecast weatherForecast;
    @Autowired
    WeatherForecastRepository weatherForecastRepository;

    @Override
    public WeatherInfo getCurrentWeather(String cityName) throws JSONException {
        String capitalize=cityName.substring(0,1).toUpperCase()+cityName.substring(1);                      //Capitalize the String
        if(weatherInfoRepository.existsById(capitalize)){
            weatherInfo=weatherInfoRepository.findById(capitalize).get();
            Duration duration=Duration.between(weatherInfo.getLastUpdated(), LocalDateTime.now());
            if(duration.toMinutes()>8)                                                            //If greater than 8 minutes update weather details
                weatherInfoInterface.updateCurrentWeather(capitalize);
            return weatherInfoInterface.cacheCityWeather(capitalize);                             //Creating new cache by fetching updated current weather
        }
        else
            return weatherInfoInterface.cacheCityWeather(capitalize);
    }

    @Override
    @Cacheable(value = "cacheWeather",key = "#cityName")
    public WeatherInfo cacheCityWeather(String cityName) throws JSONException {
        return weatherInfoRepository.saveAndFlush(weatherInfoInterface.fetchCurrentWeatherFromOpenWeather(cityName));
    }

    @Override
    public WeatherInfo fetchCurrentWeatherFromOpenWeather(String cityName) throws JSONException {
        String openWeatherURI = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;
        RestTemplate restTemplate=new RestTemplate();
        String jsonResponse=restTemplate.getForObject(openWeatherURI, String.class);
        weatherInfo.setCityName(cityName);
        JSONArray weatherResponse=new JSONObject(jsonResponse).getJSONArray("weather");
        weatherInfo.setCurrentWeather(weatherResponse.getJSONObject(0).get("main").toString());
        weatherInfo.setWeatherDescription(weatherResponse.getJSONObject(0).get("description").toString());
        JSONObject mainResponse=new JSONObject(jsonResponse).getJSONObject("main");
        weatherInfo.setTemperature(mainResponse.getDouble("temp"));
        weatherInfo.setTempMin(mainResponse.getDouble("temp_min"));
        weatherInfo.setTempMax(mainResponse.getDouble("temp_max"));
        weatherInfo.setCelsius(Double.valueOf(decimalFormat.format(mainResponse.getDouble("temp")-273.15)));
        weatherInfo.setFahrenheit(Double.valueOf(decimalFormat.format((((mainResponse.getDouble("temp")-273.15)*9)/5)+32)));
        JSONObject windResponse=new JSONObject(jsonResponse).getJSONObject("wind");
        weatherInfo.setWindSpeed(windResponse.getDouble("speed"));
        weatherInfo.setLastUpdated(LocalDateTime.now());
        weatherInfo.setIcon(weatherResponse.getJSONObject(0).get("icon").toString());
        return weatherInfo;
    }

    @Override
    @CacheEvict(value = "cacheWeather",key = "#cityName")
    public void updateCurrentWeather(String cityName) {
        weatherInfoRepository.deleteById(cityName);
    }

    @Override
    public WeatherForecast weekForecast(String cityName) throws JSONException {
        String capitalize=cityName.substring(0,1).toUpperCase()+cityName.substring(1);                      //Capitalize the String
        if(weatherForecastRepository.existsByCityName(capitalize)){
            weatherForecast=weatherForecastRepository.findByCityName(capitalize);
            Duration duration=Duration.between(weatherForecast.getLastUpdated(), LocalDateTime.now());
            if(duration.toMinutes()>50)           //Call update weather forecast method to update forecast..
                weatherInfoInterface.updateWeatherForecast(capitalize);
            return weatherInfoInterface.cacheCityWeatherForecast(capitalize);
        }
        else
            return  weatherInfoInterface.cacheCityWeatherForecast(capitalize);
    }

    @Override
    @Cacheable(value = "cacheForecast",key = "#cityName")
    public WeatherForecast cacheCityWeatherForecast(String cityName) throws JSONException {
        return weatherForecastRepository.save(weatherInfoInterface.fetchWeatherForecastFormOpenWeather(cityName));
    }

    @Override
    public WeatherForecast fetchWeatherForecastFormOpenWeather(String cityName) throws JSONException {
        String openWeatherURI="http://api.openweathermap.org/data/2.5/forecast?q="+cityName+"&appid="+apiKey;
        RestTemplate restTemplate=new RestTemplate();
        String jsonResponse=restTemplate.getForObject(openWeatherURI,String.class);
        weatherForecast.set_id(UUID.randomUUID().toString());
        weatherForecast.setCityName(cityName);
        //Need to iterate for 40 times to fetch all values from JSON response.
        JSONArray jsonArray=new JSONObject(jsonResponse).getJSONArray("list");
        List<ForeCastForWeek> foreCastForWeekList=new LinkedList<>();
        for(int i=0;i<jsonArray.length();i++){
            ForeCastForWeek foreCastForWeek=new ForeCastForWeek();
            JSONObject jsonObject= (JSONObject) jsonArray.get(i);
            JSONArray weatherResponse=jsonObject.getJSONArray("weather");
            foreCastForWeek.setCurrentWeather(weatherResponse.getJSONObject(0).get("main").toString());
            foreCastForWeek.setWeatherDescription(weatherResponse.getJSONObject(0).get("description").toString());
            foreCastForWeek.setTemperature(jsonObject.getJSONObject("main").getDouble("temp"));
            foreCastForWeek.setTempMin(Double.valueOf(decimalFormat.format(jsonObject.getJSONObject("main").getDouble("temp_min")-273.15)));
            foreCastForWeek.setTempMax(Double.valueOf(decimalFormat.format(jsonObject.getJSONObject("main").getDouble("temp_max")-273.15)));
            foreCastForWeek.setHumidity(jsonObject.getJSONObject("main").getDouble("humidity"));
            foreCastForWeek.setCelsius(Double.valueOf(decimalFormat.format(jsonObject.getJSONObject("main").getDouble("temp")-273.15)));
            foreCastForWeek.setFahrenheit(Double.valueOf(decimalFormat.format((((jsonObject.getJSONObject("main").getDouble("temp")-273.15)*9)/5)+32)));
            foreCastForWeek.setWindSpeed(jsonObject.getJSONObject("wind").getDouble("speed"));
            String dateAndTime=jsonObject.get("dt_txt").toString();
            LocalDate date=LocalDate.parse(dateAndTime.split(" ")[0]);
            LocalTime time= LocalTime.parse(dateAndTime.split(" ")[1]);
            foreCastForWeek.setDate(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            foreCastForWeek.setTime(time.format(DateTimeFormatter.ofPattern("hh:mm a")));
            foreCastForWeek.setIcon(weatherResponse.getJSONObject(0).getString("icon"));
            foreCastForWeekList.add(foreCastForWeek);
        }
        weatherForecast.setForeCastForWeekList(foreCastForWeekList);
        weatherForecast.setLastUpdated(LocalDateTime.now());
        return weatherForecast;
    }

    @Override
    @CacheEvict(value = "cacheForecast",key = "#cityName")
    public void updateWeatherForecast(String cityName) {
        weatherForecastRepository.deleteById(weatherForecastRepository.findByCityName(cityName).get_id());
    }
}
