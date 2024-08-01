package com.example.notificationservice.elasticsearch;

import com.example.notificationservice.message.Message;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
        sendSMSDetails.setPhone_number(message.getPhone_number());
        sendSMSDetails.setMessage(message.getMessage());
        sendSMSDetails.setStatus(message.getStatus());
        sendSMSDetails.setFailure_code(message.getFailure_code());
        sendSMSDetails.setFailure_comments(message.getFailure_comments());
        sendSMSDetails.setCreated(message.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli());
        sendSMSDetails.setUpdated(message.getUpdatedAt().toInstant(ZoneOffset.UTC).toEpochMilli());
        sendSMSDetailsRepository.save(sendSMSDetails);
    }

    public Page<SendSMSDetails> findSMSContainingText(String text, int page, int size) {
            PageRequest pageable = PageRequest.of(page, size);
            return sendSMSDetailsRepository.findSendSMSDetailsByMessageIsContaining(text, pageable);
    }

    public Page<SendSMSDetails> findSMSBetween(LocalDateTime startTime, LocalDateTime endTime, int page, int size) {

            PageRequest pageable = PageRequest.of(page, size);
            Long start = startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
            Long end = endTime.toInstant(ZoneOffset.UTC).toEpochMilli();
            return sendSMSDetailsRepository.findSendSMSDetailsByCreatedBetween(start, end, pageable);
    }
}
