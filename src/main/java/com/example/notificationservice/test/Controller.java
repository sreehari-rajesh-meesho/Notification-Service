package com.example.notificationservice.test;

import com.example.notificationservice.message.Message;
import com.example.notificationservice.notificationservice.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/sms")
@AllArgsConstructor
public class Controller {

    private NotificationService notificationService;

    @PostMapping(path = "send")
    public void sendSMS(@RequestBody SMSRequest smsRequest) {
            System.out.println(smsRequest.getMessage());
            System.out.println(smsRequest.getPhoneNumber());
            System.out.println("SMS request: " + smsRequest);
            Message message = new Message();
            message.setMessage(smsRequest.getMessage());
            message.setPhone_number(smsRequest.getPhoneNumber());
            notificationService.IngestionOperation(message);
    }

}
