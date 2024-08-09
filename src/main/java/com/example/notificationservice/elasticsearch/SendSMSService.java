package com.example.notificationservice.elasticsearch;

import com.example.notificationservice.message.Message;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class SendSMSService {

    private final SendSMSDetailsRepository sendSMSDetailsRepository;

    public Page<SendSMSDetails> findByPage(int page, int size) {
            PageRequest pageable = PageRequest.of(page, size);
            return sendSMSDetailsRepository.findAll(pageable);
    }

    public void saveMessage(Message message) {
        SendSMSDetails sendSMSDetails = new SendSMSDetails();
        sendSMSDetails.setPhoneNumber(message.getPhoneNumber());
        sendSMSDetails.setMessage(message.getMessage());
        sendSMSDetails.setStatus(message.getStatus());
        sendSMSDetails.setFailureCode(message.getFailureCode());
        sendSMSDetails.setFailureComments(message.getFailureComments());
        sendSMSDetails.setCreated(message.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli());
        sendSMSDetails.setUpdated(message.getUpdatedAt().toInstant(ZoneOffset.UTC).toEpochMilli());
        sendSMSDetailsRepository.save(sendSMSDetails);
    }

    public Page<SendSMSDetails> findSMSContainingText(String text, int page, int size) {
            PageRequest pageable = PageRequest.of(page, size);
            List<SendSMSDetails>smsDetailsList = sendSMSDetailsRepository.findAll(pageable).getContent();
            List<SendSMSDetails> smsDetailsMatchesRegex = new ArrayList<>();
            for(SendSMSDetails smsDetails : smsDetailsList) {
                if(Pattern.matches(".*" + text + ".*", smsDetails.getMessage())) {
                    smsDetailsMatchesRegex.add(smsDetails);
                    System.out.println("sms Details:"+smsDetails.toString());
                }
            }
            int start = (int) pageable.getOffset();
            int end = Math.min(start+ pageable.getPageSize(), smsDetailsMatchesRegex.size());
            return new PageImpl<>(smsDetailsMatchesRegex.subList(start, end), pageable, smsDetailsMatchesRegex.size());
    }

    public Page<SendSMSDetails> findSMSBetween(LocalDateTime startTime, LocalDateTime endTime, String phoneNumber, int page, int size) {
            PageRequest pageable = PageRequest.of(page, size);
            Long start = startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
            Long end = endTime.toInstant(ZoneOffset.UTC).toEpochMilli();
            return sendSMSDetailsRepository.findSendSMSDetailsByCreatedBetweenAndPhoneNumber(start, end, phoneNumber, pageable);
    }
}
