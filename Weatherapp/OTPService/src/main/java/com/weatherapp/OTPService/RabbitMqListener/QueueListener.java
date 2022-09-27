package com.weatherapp.OTPService.RabbitMqListener;

import com.weatherapp.OTPService.Model.OTPBucket;
import com.weatherapp.OTPService.RabbitMqConfig.OTPQueue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class QueueListener {

    @Autowired JavaMailSender javaMailSender;
    SimpleMailMessage simpleMailMessage=new SimpleMailMessage();

    @RabbitListener(queues = OTPQueue.QUEUE)
    public void OTPQueueListener(OTPBucket otpBucket){
        simpleMailMessage.setFrom("evento.bookconfirmation@gmail.com");
        simpleMailMessage.setTo(otpBucket.get_id());
        simpleMailMessage.setSubject("Weather app - OTP to reset password");
        simpleMailMessage.setText(" Please enter the below mentioned One Time Password in OTP field to reset your app password.."
                +"\n \n OTP : "+otpBucket.getOtpPin()
                +"\n \n Don't share this OTP to anyone..");
        javaMailSender.send(simpleMailMessage);
        System.out.println("Email has been sent to "+otpBucket.get_id());
    }
}
