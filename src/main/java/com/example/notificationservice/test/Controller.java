package com.example.notificationservice.test;


import com.example.notificationservice.elasticsearch.SendSMSDetails;
import com.example.notificationservice.notificationservice.ServiceEngine;
import com.example.notificationservice.utils.RequestNumberList;
import com.example.notificationservice.utils.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("v1")
@AllArgsConstructor
public class Controller {

    private ServiceEngine serviceEngine;

    @PostMapping(path = "sms/send")
    public String sendSMS(@RequestBody SMSRequest smsRequest) {
            System.out.println(smsRequest.getMessage());
            System.out.println(smsRequest.getPhoneNumber());
            return serviceEngine.SendSMS(smsRequest);
    }

    @PostMapping(path = "blacklist")
    public String blackListNumber(@RequestBody RequestNumberList phoneNumbers) {
            return serviceEngine.BlackListNumbers(phoneNumbers);
    }

    @DeleteMapping(path = "blacklist")
    public String deleteFromBlackList(@RequestBody RequestNumberList phoneNumbers) {
            return serviceEngine.WhiteListNumbers(phoneNumbers);
    }

    @GetMapping(path = "sms/{request_id}")
    public String getSMSById(@PathVariable("request_id") Long requestId) {
            return serviceEngine.GetSMSDetails(requestId);
    }

    @GetMapping(path = "contains/{page}/{size}")
    public Page<SendSMSDetails> getSMSDetailsContaining(@PathVariable Integer page, @PathVariable Integer size, @RequestParam String text) {
            return serviceEngine.GetSMSDetailsContainingText(page, size, text);
    }

    @GetMapping(path = "between/{page}/{size}")
    public Page<SendSMSDetails> getSMSDetailsBetween(@PathVariable Integer page, @PathVariable Integer size, @RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
            return serviceEngine.GetSMSDetailsBetween(page, size, from, to);
    }

    @GetMapping(path = "all/{page}/{size}")
    public Page<SendSMSDetails> getAllSMSDetails(@PathVariable int page, @PathVariable int size) {
            return serviceEngine.GetAllSMSDetails(page, size);
    }
}

