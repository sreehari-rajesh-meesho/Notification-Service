package com.example.notificationservice.notificationservice;

import com.example.notificationservice.elasticsearch.SendSMSDetails;
import com.example.notificationservice.message.Message;
import com.example.notificationservice.utils.RequestNumberList;
import com.example.notificationservice.utils.*;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.notificationservice.utils.Constants.*;

@Service
@AllArgsConstructor
public class ServiceEngine {

        private final NotificationService notificationService;

        private static FailureResponse<ResponseErrorObject> getResponseErrorObjectFailureResponse(Long status) {

            FailureResponse<ResponseErrorObject> failureResponse = new FailureResponse<>();

            if(status == INVALID_REQUEST) {
                ResponseErrorObject error = new ResponseErrorObject("INVALID_REQUEST", "Invalid request");
                failureResponse.setError(error);
            }
            else if(status == DATABASE_ERROR) {
                ResponseErrorObject error = new ResponseErrorObject("REQUEST_FAILED", "Database Error");
                failureResponse.setError(error);
            }
            else if(status == MESSAGE_WITH_ID_NOT_FOUND) {
                ResponseErrorObject error = new ResponseErrorObject("REQUEST_FAILED", "Message With Id Not Found");
                failureResponse.setError(error);
            }

            return failureResponse;
        }

        public String SendSMS(SMSRequest smsRequest) {

            Message message = new Message();

            message.setPhone_number(smsRequest.getPhoneNumber());
            message.setMessage(smsRequest.getMessage());

            Long messageId = notificationService.MessageIngestionPhase(message);

            if(messageId < 0) {
                FailureResponse<ResponseErrorObject> failureResponse = getResponseErrorObjectFailureResponse(messageId);
                return new Gson().toJson(failureResponse);
            }

            SuccessResponse<ResponseDataObject> successResponse = new SuccessResponse<>();
            ResponseDataObject data = new ResponseDataObject(messageId, "Pending");
            successResponse.setData(data);

            return new Gson().toJson(successResponse);

        }

        public String BlackListNumbers(RequestNumberList requestNumberList) {

            Long status = notificationService.BlackListNumbers(requestNumberList);

            if(status == BLACKLISTED_SUCCESSFULLY) {
                    SuccessResponse<String> successResponse = new SuccessResponse<>();
                    successResponse.setData("Successfully Blacklisted");
                    return new Gson().toJson(successResponse);
            } else {
                 return "Error";
            }
        }

        public String WhiteListNumbers(RequestNumberList requestNumberList) {

            Long status = notificationService.WhiteListNumbers(requestNumberList);
            if(status == WHITELISTED_SUCCESSFULLY) {
                    SuccessResponse<String> successResponse = new SuccessResponse<>();
                    successResponse.setData("Successfully Whitelisted");
                    return new Gson().toJson(successResponse);
            }   else {
                return "Error";
            }
        }

        public String GetSMSDetails(Long messageId) {

                SuccessResponse<Message> successResponse = new SuccessResponse<>();
                Optional<Message> msgById = notificationService.getMessageById(messageId);

                if(msgById.isPresent()) {
                    successResponse.setData(msgById.get());
                    return new Gson().toJson(successResponse);
                }

                return new Gson().toJson(getResponseErrorObjectFailureResponse(INVALID_REQUEST));
        }

        public Page<SendSMSDetails> GetSMSDetailsContainingText(Integer page, Integer size, String text) {
                return notificationService.getSendSMSDetailsContainingText(page, size, text);
        }

        public Page<SendSMSDetails> GetSMSDetailsBetween(Integer page, Integer size, LocalDateTime from, LocalDateTime to) {
                return notificationService.getSendSMSDetailsBetween(page, size, from, to);
        }

        public Page<SendSMSDetails> GetAllSMSDetails(Integer page, Integer size) {
                return notificationService.getAllSMSDetails(page, size);
        }

}
