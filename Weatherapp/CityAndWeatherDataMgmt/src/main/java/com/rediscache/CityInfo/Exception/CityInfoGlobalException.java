package com.rediscache.CityInfo.Exception;

import com.rediscache.CityInfo.Model.ExceptionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CityInfoGlobalException {
    @Autowired ExceptionTemplate exceptionTemplate;

    @ExceptionHandler(value = CityInfoException.class)
    public ResponseEntity<ExceptionTemplate> exceptionHandler(CityInfoException cityInfoException){
        exceptionTemplate.setErrorCode(String.valueOf(HttpStatus.NOT_FOUND));
        exceptionTemplate.setMessage(cityInfoException.getMessage());
        exceptionTemplate.setTime(LocalDateTime.now());
        return new ResponseEntity<>(exceptionTemplate, HttpStatus.NOT_FOUND);
    }
}
