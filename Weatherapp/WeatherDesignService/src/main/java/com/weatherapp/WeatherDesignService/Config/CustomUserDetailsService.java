package com.weatherapp.WeatherDesignService.Config;

import com.weatherapp.WeatherDesignService.Model.AccountPrincipal;
import com.weatherapp.WeatherDesignService.Repository.AccountPrincipalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired AccountPrincipalRepository accountPrincipalRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AccountPrincipal> account=accountPrincipalRepository.findById(username);
        if(account.isEmpty())
            throw new UsernameNotFoundException("User email id is not found database..");
        return new User(account.get().get_id(),account.get().getPassword(),new LinkedList<>());
    }
}
