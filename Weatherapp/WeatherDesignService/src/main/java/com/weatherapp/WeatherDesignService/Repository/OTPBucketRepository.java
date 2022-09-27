package com.weatherapp.WeatherDesignService.Repository;

import com.weatherapp.WeatherDesignService.Model.OTPBucket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPBucketRepository extends MongoRepository<OTPBucket,String> {

}
