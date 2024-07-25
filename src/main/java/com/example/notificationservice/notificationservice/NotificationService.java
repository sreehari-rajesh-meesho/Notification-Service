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

import static com.example.notificationservice.utils.Constants.*;


@Service
@AllArgsConstructor
public class NotificationService {

        private final MessageService messageService;
        private final KafkaProducer kafkaProducer;
        private final BlackListedNumberService blackListedNumberService;
        private final SendSMSService sendSMSService;

        public void IngestionOperation(Message message) {
                Integer messageId = messageService.IngestMessageToDatabase(message);
                kafkaProducer.PublishMessageId("send-sms", messageId.longValue());
        }

        private SendSMSDetails getSendSMSDetails(Message message) {
            SendSMSDetails sendSMSDetails = new SendSMSDetails();
            sendSMSDetails.setId(message.getId());
            sendSMSDetails.setMessage(message.getMessage());
            sendSMSDetails.setStatus(message.getStatus());
            sendSMSDetails.setFailure_code(message.getFailure_code());
            sendSMSDetails.setFailure_comments(message.getFailure_comments());
            sendSMSDetails.setPhone_number(message.getPhone_number());
            sendSMSDetails.setCreated_at(message.getCreated_at());
            sendSMSDetails.setUpdated_at(message.getUpdated_at());
            return sendSMSDetails;
        }

        @KafkaListener(topics = "send-sms", groupId = "notify-group-id")
        public Integer RetrievalAndUpdateOperation(Long messageId) {

                Message message = messageService.getMessageById(messageId);
                Long phoneNumber = message.getPhone_number();
                BlackListedNumber blackListedNumber = new BlackListedNumber(phoneNumber.longValue());
                Boolean isNumberBlackListed = blackListedNumberService.checkIfBlackListedNumber(blackListedNumber);

                if (isNumberBlackListed) {
                    return NUMBER_BLACKLISTED;
                }

                // TODO: Call third party api
                // TODO: Update the message object

                messageService.UpdateMessageInDatabase(messageId, message.getStatus(), message.getFailure_code(), message.getFailure_comments());

                SendSMSDetails sendSMSDetails = getSendSMSDetails(message);
                sendSMSService.saveMessage(sendSMSDetails);

                return SUCCESS;
        }


}
