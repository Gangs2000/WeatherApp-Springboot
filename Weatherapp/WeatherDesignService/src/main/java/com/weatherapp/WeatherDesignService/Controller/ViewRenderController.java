package com.weatherapp.WeatherDesignService.Controller;

import com.weatherapp.WeatherDesignService.Model.AccountPrincipal;
import com.weatherapp.WeatherDesignService.Model.OTPBucket;
import com.weatherapp.WeatherDesignService.Repository.AccountPrincipalRepository;
import com.weatherapp.WeatherDesignService.Repository.OTPBucketRepository;
import com.weatherapp.WeatherDesignService.Service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin(origins = "*")
public class ViewRenderController {

    @Autowired AccountPrincipalRepository accountPrincipalRepository;
    @Autowired ResponseService responseService;
    @Autowired AccountPrincipal accountPrincipal;
    @Autowired OTPBucket otpBucket;
    @Autowired OTPBucketRepository otpBucketRepository;

    BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping(path = "/sendOTP", consumes = "application/x-www-form-urlencoded")
    public String forgotPIN(HttpServletRequest request,ModelMap modelMap){
        if(accountPrincipalRepository.existsById(request.getParameter("username"))) {
            responseService.updatePIN(request.getParameter("username"));
            modelMap.addAttribute("emailId", request.getParameter("username"));
            return "passwordReset";
        }
        else
            return "unauthorized";
    }

    @PostMapping(path="/verifyOTP", consumes = "application/x-www-form-urlencoded")
    public String verifyPIN(HttpServletRequest request){
        if(otpBucket.getOtpPin().equals(request.getParameter("otp"))){
            responseService.resetPIN(request.getParameter("username"), encoder.encode(request.getParameter("password")));
            return "successStatus";
        }
        else {
            otpBucketRepository.deleteById(request.getParameter("username"));
            return "failureStatus";
        }
    }

    @GetMapping("/")
    public String weatherAppDashboard(HttpSession httpSession, ModelMap modelMap){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        httpSession.setAttribute("sessionEmailId", authentication.getName());
        httpSession.setAttribute("sessionLocation", accountPrincipalRepository.findById(authentication.getName()).get().getLocation());
        modelMap.addAttribute("location", accountPrincipalRepository.findById(authentication.getName()).get().getLocation());
        responseService.setModelAttributes(modelMap);
        return "dashboard";
    }

    @PostMapping(path = "/registration", consumes = "application/x-www-form-urlencoded")
    public String registerNewAccount(HttpServletRequest request, ModelMap modelMap){
        if(accountPrincipalRepository.existsById(request.getParameter("userName")))
            modelMap.addAttribute("message","Email ID is already registered..");
        else {
            accountPrincipal.set_id(request.getParameter("userName"));
            accountPrincipal.setPassword(encoder.encode(request.getParameter("password")));
            accountPrincipal.setLocation(request.getParameter("location"));
            accountPrincipalRepository.save(accountPrincipal);
            modelMap.addAttribute("message", "Registration successfully done..");
        }
        return "login";
    }

    @PostMapping(path="/fetchWeather", consumes = "application/x-www-form-urlencoded")
    public String fetchWeatherDetails(HttpServletRequest request, ModelMap modelMap){
        String cityName=request.getParameter("cityName").substring(0,1).toUpperCase()+request.getParameter("cityName").substring(1);        //Capitalize the String
        modelMap.addAttribute("location", cityName);
        responseService.setModelAttributes(modelMap);
        return "dashboard";
    }

    @GetMapping("/logout-success")
    public String logoutPage(){
        return "logout";
    }
}
