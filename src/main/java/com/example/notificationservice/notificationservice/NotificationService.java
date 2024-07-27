package com.example.notificationservice.notificationservice;

import com.example.notificationservice.elasticsearch.SendSMSDetails;
import com.example.notificationservice.elasticsearch.SendSMSService;
import com.example.notificationservice.kafka.KafkaProducer;
import com.example.notificationservice.message.Message;
import com.example.notificationservice.message.MessageService;
import com.example.notificationservice.redis.BlackListedNumber;
import com.example.notificationservice.redis.BlackListedNumberService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.notificationservice.utils.Constants.*;


@Service
@AllArgsConstructor
public class NotificationService {

        private final MessageService messageService;
        private final KafkaProducer kafkaProducer;
        private final BlackListedNumberService blackListedNumberService;
        private final SendSMSService sendSMSService;

        public void IngestionOperation(Message message) {
                message.setCreated_at(LocalDateTime.now());
                Integer messageId = messageService.IngestMessageToDatabase(message);
                kafkaProducer.PublishMessageId("send-sms", messageId.toString());
        }

        private SendSMSDetails getSendSMSDetails(Message message) {
            SendSMSDetails sendSMSDetails = new SendSMSDetails(
                    message.getId(),
                    message.getPhone_number(),
                    message.getMessage(),
                    message.getStatus(),
                    message.getFailure_code(),
                    message.getFailure_comments(),
                    message.getCreated_at().toString(),
                    message.getUpdated_at().toString()
            );
            return sendSMSDetails;
        }

        @KafkaListener(topics = "send-sms", groupId = "notify-group-id")
        public Integer RetrievalAndUpdateOperation(String messageID) {

                Long messageId = Long.parseLong(messageID);
                Message message = messageService.getMessageById(messageId);
                Long phoneNumber = message.getPhone_number();
                BlackListedNumber blackListedNumber = new BlackListedNumber(phoneNumber);
                Boolean isNumberBlackListed = blackListedNumberService.checkIfBlackListedNumber(blackListedNumber);

                if (isNumberBlackListed) {
                    return NUMBER_BLACKLISTED;
                }

                // TODO: Call third party api
                // TODO: Update the message object

                message.setStatus(404);
                message.setFailure_code("Internal Server Error");
                message.setFailure_comments("API call failed");

                messageService.UpdateMessageInDatabase(messageId, message.getStatus(), message.getFailure_code(), message.getFailure_comments());
                message.setUpdated_at(LocalDateTime.now());

                SendSMSDetails sendSMSDetails = getSendSMSDetails(message);
                sendSMSService.saveMessage(sendSMSDetails);

                return SUCCESS;
        }

        public Iterable<SendSMSDetails> sendSMSDetails() {
                return sendSMSService.findAll();
        }

}
