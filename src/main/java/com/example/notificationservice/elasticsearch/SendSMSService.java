package com.example.notificationservice.elasticsearch;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class SendSMSService {

    private final SendSMSDetailsRepository sendSMSDetailsRepository;

    public Iterable<SendSMSDetails> findAll() {
         return sendSMSDetailsRepository.findAll();
    }

    public void saveMessage(SendSMSDetails sendSMSDetails) {
        System.out.println(sendSMSDetails);
        sendSMSDetailsRepository.save(sendSMSDetails);
    }
}
