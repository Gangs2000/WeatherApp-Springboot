package com.rediscache.CityInfo.Controller;


import com.rediscache.CityInfo.Model.CityData;
import com.rediscache.CityInfo.Service.CityInfoService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityInfoController {

    @Autowired CityInfoService cityInfoService;

    @PostMapping("/add/{cityName}")
    public ResponseEntity<CityData> addNewCity(@PathVariable("cityName") String cityName) throws JSONException {
        return new ResponseEntity<>(cityInfoService.addNewCity(cityName), HttpStatus.CREATED);
    }

    @GetMapping("/get-all-cities")
    public ResponseEntity<List<CityData>> getAllCities(){
        return new ResponseEntity<>(cityInfoService.fetchAllCityDetails(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-city/id/{id}")
    public ResponseEntity<CityData> getCityDetailsById(@PathVariable("id") long id){
        return new ResponseEntity<>(cityInfoService.fetchCityById(id), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-city/city/{cityName}")
    public ResponseEntity<CityData> getCityDetailsByName(@PathVariable("cityName") String cityName) throws JSONException {
        return new ResponseEntity<>(cityInfoService.fetchCityByName(cityName), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-city/city/{cityName}")
    public ResponseEntity<String> deleteCityDetailsByName(@PathVariable("cityName") String cityName){
        return new ResponseEntity<>(cityInfoService.deleteCityDetails(cityName), HttpStatus.ACCEPTED);
    }
}
