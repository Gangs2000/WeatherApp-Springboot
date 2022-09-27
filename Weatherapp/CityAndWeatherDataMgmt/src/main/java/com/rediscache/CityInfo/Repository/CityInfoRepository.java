package com.rediscache.CityInfo.Repository;

import com.rediscache.CityInfo.Model.CityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityInfoRepository extends JpaRepository<CityData, Long> {
    boolean existsByCityName(String cityName);
    CityData findByCityName(String cityName);
}
