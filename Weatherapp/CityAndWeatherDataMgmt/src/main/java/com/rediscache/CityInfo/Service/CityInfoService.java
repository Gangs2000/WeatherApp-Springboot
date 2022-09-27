package com.rediscache.CityInfo.Service;

import com.rediscache.CityInfo.Exception.CityInfoException;
import com.rediscache.CityInfo.Interface.CityInfoInterface;
import com.rediscache.CityInfo.Repository.CityInfoRepository;
import com.rediscache.CityInfo.Model.CityData;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityInfoService implements CityInfoInterface {

    private final String apiKey="**************************************************";

    @Autowired
    CityInfoRepository cityInfoRepository;
    @Autowired CityInfoInterface cityInfoInterface;
    @Autowired CityData cityData;

    @Override
    @Caching(put = {@CachePut(value = "cacheCity", key = "#cityName")}, evict = {@CacheEvict(value = "cacheCities", allEntries = true)})
    public CityData addNewCity(String cityName) throws JSONException {
        //Saving all City Details in Postgresql DB after fetching JSON response from Open Weather application
        if(cityInfoRepository.existsByCityName(cityName))
            throw new CityInfoException("Save operation aborted since city is already existed in database..");
        else {
            String openWeatherURI = "http://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&mode=json&appid=" + apiKey + "&units=metric";
            RestTemplate restTemplate = new RestTemplate();
            String jsonResponse = restTemplate.getForObject(openWeatherURI, String.class);
            JSONObject jsonObject = new JSONObject(jsonResponse).getJSONObject("city");
            cityData.setCityId(jsonObject.getLong("id"));
            cityData.setCityName(jsonObject.get("name").toString());
            cityData.setCountry(jsonObject.get("country").toString());
            cityData.setLatitude(jsonObject.getJSONObject("coord").getLong("lat"));
            cityData.setLongitude(jsonObject.getJSONObject("coord").getLong("lon"));
            cityData.setPopulation(jsonObject.getLong("population"));
            cityData.setTimeZone(jsonObject.getLong("timezone"));
            cityData.setSunRise(jsonObject.getLong("sunrise"));
            cityData.setSunSet(jsonObject.getLong("sunset"));
            return cityInfoRepository.saveAndFlush(cityData);
        }
    }

    @Override
    @Cacheable(value = "cacheCities")
    public List<CityData> fetchAllCityDetails() {
        //Sorting all city details by its population
        System.out.println("Fetched list of all cities from DB");
        return cityInfoRepository.findAll().stream().sorted((o1, o2) -> (o1.getPopulation()>o2.getPopulation())?(-1):(1)).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "cacheCity", key = "#id")
    public CityData fetchCityById(long id) {
        //Fetching city details by its ID
        System.out.println("Fetch city details by ID from DB");
        if(cityInfoRepository.existsById(id))
            return cityInfoRepository.findById(id).get();
        else
            throw new CityInfoException("Get operation aborted since, City ID doesn't exist in database..");
    }

    @Override
    @Cacheable(value = "cacheCity", key = "#cityName")
    public CityData fetchCityByName(String cityName) throws JSONException {
        //Fetching city details by city name
        if(cityInfoRepository.existsByCityName(cityName))
            return cityInfoRepository.findByCityName(cityName);
        else
            return cityInfoInterface.addNewCity(cityName);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "cacheCity", key = "#cityName"), @CacheEvict(value = "cacheCities", allEntries = true)})
    public String deleteCityDetails(String cityName) {
        //Deleting city details by city name
        if(cityInfoRepository.existsByCityName(cityName)){
            cityInfoRepository.deleteById(cityInfoRepository.findByCityName(cityName).getCityId());
            return cityName+" city has been deleted from database successfully..";
        }
        else
            throw new CityInfoException("Delete operation aborted since, City Name doesn't exist in database..");
    }
}
