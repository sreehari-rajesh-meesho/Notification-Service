package com.example.notificationservice.notificationservice;

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
import java.util.Optional;

import static com.example.notificationservice.constants.Constants.*;

@Service
@AllArgsConstructor
public class NotificationService {

    public SendSMSService sendSMSService;
        public MessageService messageService;
        public BlackListedNumberService blackListedNumberService;
        public KafkaProducer kafkaProducer;

        public Long MessageIngestionPhase(Message message) {

                message.setCreated_at(LocalDateTime.now());

                Long messageId = messageService.IngestMessageToDatabase(message);

                if(messageId == PHONE_NUMBER_MANDATORY) {
                        return PHONE_NUMBER_MANDATORY;
                }

                kafkaProducer.PublishMessageId("send-sms", messageId.toString());

                return INGESTION_SUCCESSFUL;
        }

        @KafkaListener(id="notify-group-id", topics = "send-sms")
        public Long MessageSendAndUpdatePhase(String KafkaMessageId) {

                Long messageId = Long.parseLong(KafkaMessageId);

                Optional<Message> message = messageService.findMessageById(messageId);

                if(message.isPresent()) {

                    String phoneNumber = message.get().getPhone_number();
                    String messageText = message.get().getMessage();

                    if(blackListedNumberService.checkIfBlackListedNumber(new BlackListedNumber(phoneNumber))) {
                            return NUMBER_BLACKLISTED;
                    } else {
                            // Call third party API
                            Integer mockStatus = 404;
                            String mockFailureCode = "Internal Server Error";
                            String mockFailureComments = "Hello World";
                            // Update the details in the database;
                            Long updatedMessageId = messageService.UpdateMessageInDatabase(messageId, mockStatus, mockFailureCode, mockFailureComments);

                            Optional<Message> updatedMessage = messageService.findMessageById(updatedMessageId);

                            if(updatedMessage.isPresent()) {
                                // Ingest the details in elastic search
                                sendSMSService.saveMessage(updatedMessage.get());
                            } else {
                                return MESSAGE_WITH_ID_NOT_FOUND;
                            }

                            return UPDATE_SUCCESSFUL;
                    }


                } else {
                        return MESSAGE_WITH_ID_NOT_FOUND;
                }
        }
}
