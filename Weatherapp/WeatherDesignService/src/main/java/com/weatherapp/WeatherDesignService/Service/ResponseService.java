package com.weatherapp.WeatherDesignService.Service;

import com.weatherapp.WeatherDesignService.FeignClientInterface.FeignClientResponseInterface;
import com.weatherapp.WeatherDesignService.Interface.ResponseInterface;
import com.weatherapp.WeatherDesignService.Model.*;
import com.weatherapp.WeatherDesignService.RabbitMqConfig.OTPQueue;
import com.weatherapp.WeatherDesignService.Repository.AccountPrincipalRepository;
import com.weatherapp.WeatherDesignService.Repository.OTPBucketRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class ResponseService implements ResponseInterface {

    @Autowired AccountPrincipal accountPrincipal;
    @Autowired AccountPrincipalRepository accountPrincipalRepository;
    @Autowired OTPBucket otpBucket;
    @Autowired OTPBucketRepository otpBucketRepository;
    @Autowired RabbitTemplate rabbitTemplate;
    @Autowired FeignClientResponseInterface feignClientResponseInterface;

    @Autowired ResponseInterface responseInterface;
    DecimalFormat decimalFormat=new DecimalFormat("#.00");

    @Override
    public void updatePIN(String emailId) {
        Random random=new Random();
        otpBucket.set_id(emailId);
        otpBucket.setOtpPin(String.format("%05d",random.nextInt(99999)));
        //Producing Object details into Rabbit Queue
        rabbitTemplate.convertAndSend(OTPQueue.EXCHANGE,OTPQueue.ROUTING_KEY,otpBucket);
        otpBucketRepository.save(otpBucket);
    }

    @Override
    public void resetPIN(String emailId, String password) {
        accountPrincipal=accountPrincipalRepository.findById(emailId).get();
        accountPrincipal.setPassword(password);
        //Updating account with new password..
        accountPrincipalRepository.save(accountPrincipal);
        //Deleting OTP details from DB after updating password..
        otpBucketRepository.deleteById(emailId);
    }

    @Override
    public CityData getCityData(String cityName) {
        CityData cityData=feignClientResponseInterface.getCityData(cityName);
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneOffset.ofTotalSeconds((int) cityData.getTimeZone()));
        cityData.setSunRise(dateTimeFormatter.format(Instant.ofEpochSecond((Integer) cityData.getSunRise())));
        cityData.setSunSet(dateTimeFormatter.format(Instant.ofEpochSecond((Integer) cityData.getSunSet())));
        return cityData;
    }

    @Override
    public WeatherInfo getCurrentWeather(String cityName) {
        return feignClientResponseInterface.getCurrentWeather(cityName);
    }

    @Override
    public WeatherForecast getWeatherForeCast(String cityName) {
        return feignClientResponseInterface.getWeatherForeCast(cityName);
    }

    @Override
    public void setModelAttributes(ModelMap modelMap) {
        CityData cityData=responseInterface.getCityData(modelMap.getAttribute("location").toString());
        modelMap.addAttribute("cityData",cityData);
        WeatherInfo weatherInfo=responseInterface.getCurrentWeather(modelMap.getAttribute("location").toString());
        modelMap.addAttribute("weatherInfo",weatherInfo);
        modelMap.addAttribute("maxTemp",Double.valueOf(decimalFormat.format(weatherInfo.getTempMax()-273.15)));
        modelMap.addAttribute("minTemp",Double.valueOf(decimalFormat.format(weatherInfo.getTempMin()-273.15)));
        modelMap.addAttribute("timeZone", ZoneOffset.ofTotalSeconds((int) cityData.getTimeZone()));
        modelMap.addAttribute("lastWeatherUpdated", Duration.between(weatherInfo.getLastUpdated(), LocalDateTime.now()).toMinutes());
        WeatherForecast weatherForecast=responseInterface.getWeatherForeCast(modelMap.getAttribute("location").toString());
        modelMap.addAttribute("listData",weatherForecast.getForeCastForWeekList());
        modelMap.addAttribute("lastForecastDataUpdated",Duration.between(weatherForecast.getLastUpdated(),LocalDateTime.now()).toMinutes());
    }
}
