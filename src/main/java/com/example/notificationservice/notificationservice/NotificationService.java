package com.example.notificationservice.notificationservice;

import com.example.notificationservice.elasticsearch.SendSMSDetails;
import com.example.notificationservice.elasticsearch.SendSMSService;
import com.example.notificationservice.kafka.KafkaProducer;
import com.example.notificationservice.message.Message;
import com.example.notificationservice.message.MessageService;
import com.example.notificationservice.redis.BlackListedNumber;
import com.example.notificationservice.redis.BlackListedNumberRepository;
import com.example.notificationservice.redis.BlackListedNumberService;
import com.example.notificationservice.thirdparty.ThirdPartyResponseBody;
import com.example.notificationservice.thirdparty.ThirdPartyService;
import com.example.notificationservice.utils.PageResponse;
import com.example.notificationservice.utils.PhoneNumberValidation;
import com.example.notificationservice.utils.RequestNumberList;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.notificationservice.utils.Constants.*;

@Service
@AllArgsConstructor
public class NotificationService {

        private final BlackListedNumberRepository blackListedNumberRepository;
        private SendSMSService sendSMSService;
        private MessageService messageService;
        private BlackListedNumberService blackListedNumberService;
        private KafkaProducer kafkaProducer;
        private ThirdPartyService thirdPartyService;
        private PhoneNumberValidation phoneNumberValidation;

        public Long MessageIngestionPhase(Message message) {

                if(phoneNumberValidation.isValidPhoneNumber(message.getPhoneNumber()) != 0L){
                        return phoneNumberValidation.isValidPhoneNumber(message.getPhoneNumber());
                }

                Long messageId = messageService.IngestMessageToDatabase(message);

                if(messageId == DATABASE_ERROR) {
                    return DATABASE_ERROR;
                }

                kafkaProducer.PublishMessageId("send-sms", messageId.toString());

                return messageId;
        }

        @KafkaListener(groupId = "notify-group-id", topics = "send-sms")
        public void MessageSendAndUpdatePhase(String KafkaMessageId) {

                Long messageId = Long.parseLong(KafkaMessageId);
                Long UpdatedMessageId = -1L;
                Optional<Message> message = messageService.findMessageById(messageId);

                System.out.println("MessageID:"+ messageId);

                if(message.isPresent()) {

                    String phoneNumber = message.get().getPhoneNumber();
                    String messageText = message.get().getMessage();

                    if(blackListedNumberService.checkIfBlackListedNumber(new BlackListedNumber(phoneNumber))) {
                            System.out.println("Phone number is blacklisted");
                            Integer status = Math.toIntExact(NUMBER_BLACKLISTED);
                            String failure_code = "NUMBER_BLACKLISTED";
                            String failure_message = "The Number is BlackListed";

                            messageService.UpdateMessageInDatabase(messageId, status, failure_code, failure_message);

                    } else {
                            ResponseEntity<ThirdPartyResponseBody> response = thirdPartyService.sendSMS(messageId, phoneNumber, messageText);
                            // Update the details in the database;

                            int status = response.getStatusCode().value();

                            ThirdPartyResponseBody thirdPartyResponseBody = response.getBody();
                            String failure_code = thirdPartyResponseBody.getResponse().getCode();
                            String failure_message = thirdPartyResponseBody.getResponse().getDescription();

                            UpdatedMessageId = messageService.UpdateMessageInDatabase(messageId, status, failure_code, failure_message);
                    }
                } else {
                        Integer status = Math.toIntExact(MESSAGE_WITH_ID_NOT_FOUND);
                        String failure_code = "MESSAGE_NOT_FOUND";
                        String failure_message = "Message with ID" + messageId + " not found";
                        UpdatedMessageId = messageService.UpdateMessageInDatabase(messageId, status, failure_code, failure_message);
                }

                System.out.println("UpdatedMessageID:" + UpdatedMessageId);
                Optional<Message> updatedMessage = messageService.findMessageById(UpdatedMessageId);

                if(updatedMessage.isPresent()) {
                        // Ingest the details in elastic search
                        sendSMSService.saveMessage(updatedMessage.get());
                        System.out.println("Indexed Details in elastic search");
                }
        }

        public Long BlackListNumbers(RequestNumberList requestNumberList) {
                List<BlackListedNumber> blNumList = new ArrayList<>();
                for(String phoneNumber : requestNumberList.getPhoneNumbers()) {
                        BlackListedNumber blNum = new BlackListedNumber(phoneNumber);
                        blNumList.add(blNum);
                }
                try{
                        blackListedNumberService.saveBlackListedNumberList(blNumList);
                        return BLACKLISTED_SUCCESSFULLY;
                } catch(Exception e){
                        return REDIS_ERROR;
                }
        }

        public Long WhiteListNumbers(RequestNumberList requestNumberList) {

                List<BlackListedNumber> wlNumList = new ArrayList<>();
                for(String phoneNumber : requestNumberList.getPhoneNumbers()) {
                        BlackListedNumber blNum = new BlackListedNumber(phoneNumber);
                        wlNumList.add(blNum);
                }
                try{
                        blackListedNumberService.deleteAllBlackListedNumbers(wlNumList);
                        return WHITELISTED_SUCCESSFULLY;
                } catch(Exception e){
                        return REDIS_ERROR;
                }
       }

        public Optional<Message> getMessageById(Long messageId) {
                return messageService.findMessageById(messageId);
        }

        public PageResponse<SendSMSDetails> getSendSMSDetailsContainingText(Integer page, Integer size, String text) {
                try{
                        Page<SendSMSDetails> smsDetailsPage = sendSMSService.findSMSContainingText(text, page, size);
                        return new PageResponse<>(null, smsDetailsPage);
                } catch (Exception e) {
                        return new PageResponse<>("Elastic Search Error", null);
                }
        }

        public PageResponse<SendSMSDetails> getSendSMSDetailsBetween(Integer page, Integer size, LocalDateTime from, LocalDateTime to) {
                try{
                        Page<SendSMSDetails> smsDetailsPage = sendSMSService.findSMSBetween(from, to, page, size);
                        return new PageResponse<>(null, smsDetailsPage);
                } catch (Exception e) {
                        return new PageResponse<>("Elastic Search Error", null);
                }
        }

        public PageResponse<SendSMSDetails> getAllSMSDetails(Integer page, Integer size) {
                try{
                        Page<SendSMSDetails> smsDetailsPage = sendSMSService.findByPage(page, size);
                        return new PageResponse<>(null, smsDetailsPage);
                } catch (Exception e) {
                        return new PageResponse<>("Elastic Search Error", null);
                }
        }
}
