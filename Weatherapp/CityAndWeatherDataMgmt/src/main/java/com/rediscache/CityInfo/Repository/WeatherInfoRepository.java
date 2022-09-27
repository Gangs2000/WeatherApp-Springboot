package com.rediscache.CityInfo.Repository;

import com.rediscache.CityInfo.Model.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, String> {

}