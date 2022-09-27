package com.rediscache.CityInfo.Interface;

import com.rediscache.CityInfo.Model.CityData;
import org.json.JSONException;

import java.util.List;

public interface CityInfoInterface {
    CityData addNewCity(String cityName) throws JSONException;
    List<CityData> fetchAllCityDetails();
    CityData fetchCityById(long id);
    CityData fetchCityByName(String cityName) throws JSONException;
    String deleteCityDetails(String cityName);
}
