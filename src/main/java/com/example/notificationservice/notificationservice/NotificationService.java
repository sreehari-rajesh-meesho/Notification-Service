package com.example.notificationservice.notificationservice;

import com.example.notificationservice.elasticsearch.SendSMSDetails;
import com.example.notificationservice.elasticsearch.SendSMSService;
import com.example.notificationservice.kafka.KafkaProducer;
import com.example.notificationservice.message.Message;
import com.example.notificationservice.message.MessageService;
import com.example.notificationservice.redis.BlackListedNumber;
import com.example.notificationservice.redis.BlackListedNumberService;
import com.example.notificationservice.thirdparty.ThirdPartyService;
import com.example.notificationservice.utils.RequestNumberList;
import com.google.gson.Gson;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.notificationservice.utils.Constants.*;

@Service
@AllArgsConstructor
public class NotificationService {

        public SendSMSService sendSMSService;
        private MessageService messageService;
        private BlackListedNumberService blackListedNumberService;
        private KafkaProducer kafkaProducer;
        private ThirdPartyService thirdPartyService;

        public Long MessageIngestionPhase(Message message) {

                Long messageId = messageService.IngestMessageToDatabase(message);

                if(messageId == PHONE_NUMBER_MANDATORY) {
                    return PHONE_NUMBER_MANDATORY;
                }

                if(messageId == DATABASE_ERROR) {
                    return DATABASE_ERROR;
                }

                kafkaProducer.PublishMessageId("send-sms", messageId.toString());

                return messageId;
        }

        @KafkaListener(groupId = "notify-group-id", topics = "send-sms")
        public void MessageSendAndUpdatePhase(String KafkaMessageId) {

                Long messageId = Long.parseLong(KafkaMessageId);

                Optional<Message> message = messageService.findMessageById(messageId);

                if(message.isPresent()) {

                    String phoneNumber = message.get().getPhone_number();
                    String messageText = message.get().getMessage();

                    if(blackListedNumberService.checkIfBlackListedNumber(new BlackListedNumber(phoneNumber))) {

                            Integer status = Math.toIntExact(NUMBER_BLACKLISTED);
                            String failure_code = "NUMBER_BLACKLISTED";
                            String failure_message = "The Number is BlackListed";

                            messageService.UpdateMessageInDatabase(messageId, status, failure_code, failure_message);

                    } else {
                            ResponseEntity<String> response = thirdPartyService.sendSMS(messageId, phoneNumber, messageText);
                            // Update the details in the database;

                            Integer status = response.getStatusCode().value();

                            JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);
                            JsonObject responseBody = jsonObject.get("response").asJsonObject();

                            String failure_code = responseBody.get("code").toString();
                            String failure_message = responseBody.get("message").toString();

                            messageService.UpdateMessageInDatabase(messageId, status, failure_code, failure_message);


                    }
                } else {
                        Integer status = Math.toIntExact(MESSAGE_WITH_ID_NOT_FOUND);
                        String failure_code = "MESSAGE_NOT_FOUND";
                        String failure_message = "Message with ID" + messageId + " not found";
                        messageService.UpdateMessageInDatabase(messageId, status, failure_code, failure_message);
                }

                Optional<Message> updatedMessage = messageService.findMessageById(messageId);

                if(updatedMessage.isPresent()) {
                        // Ingest the details in elastic search
                        sendSMSService.saveMessage(updatedMessage.get());
                        System.out.println("Indexed Details in elastic search");
                }
        }

        public Long BlackListNumbers(RequestNumberList requestNumberList) {

                List<String> phoneNumberList = requestNumberList.getPhoneNumbers();

                for(String phoneNumber : phoneNumberList) {
                        BlackListedNumber blNum = new BlackListedNumber(phoneNumber);
                        blackListedNumberService.saveBlackListedNumber(blNum);
                }

                return BLACKLISTED_SUCCESSFULLY;
        }

        public Long WhiteListNumbers(RequestNumberList requestNumberList) {

                List<String> phoneNumberList = requestNumberList.getPhoneNumbers();

                for(String phoneNumber : phoneNumberList) {
                    BlackListedNumber blNum = new BlackListedNumber(phoneNumber);
                    blackListedNumberService.deleteBlackListedNumber(blNum);
                }

                return WHITELISTED_SUCCESSFULLY;
        }

        public Optional<Message> getMessageById(Long messageId) {
                return messageService.findMessageById(messageId);
        }

        public Page<SendSMSDetails> getSendSMSDetailsContainingText(Integer page, Integer size, String text) {
                return sendSMSService.findSMSContainingText(text, page, size);
        }

        public Page<SendSMSDetails> getSendSMSDetailsBetween(Integer page, Integer size, LocalDateTime from, LocalDateTime to) {
                return sendSMSService.findSMSBetween(from, to, page, size);
        }

        public Page<SendSMSDetails> getAllSMSDetails(Integer page, Integer size) {
                return sendSMSService.findByPage(page, size);
        }
}
