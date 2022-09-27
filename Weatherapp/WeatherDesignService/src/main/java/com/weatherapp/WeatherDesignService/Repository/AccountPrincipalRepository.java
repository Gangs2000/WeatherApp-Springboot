package com.weatherapp.WeatherDesignService.Repository;

import com.weatherapp.WeatherDesignService.Model.AccountPrincipal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountPrincipalRepository extends MongoRepository<AccountPrincipal, String> {

}
