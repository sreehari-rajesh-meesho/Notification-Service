package com.example.notificationservice.controller;


import com.example.notificationservice.elasticsearch.SendSMSDetails;
import com.example.notificationservice.message.Message;
import com.example.notificationservice.notificationservice.NotificationService;
import com.example.notificationservice.utils.RequestNumberList;
import com.example.notificationservice.utils.*;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.notificationservice.utils.Constants.*;


@RestController
@RequestMapping("v1")
@AllArgsConstructor
public class Controller {

    private NotificationService notificationService;

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

    @PostMapping(path = "sms/send")
    public String sendSMS(@RequestBody SMSRequest smsRequest) {

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

    @PostMapping(path = "blacklist")
    public String blackListNumber(@RequestBody RequestNumberList phoneNumbers) {
        Long status = notificationService.BlackListNumbers(phoneNumbers);
        SuccessResponse<String> successResponse = new SuccessResponse<>();
        successResponse.setData("Successfully Blacklisted");
        return new Gson().toJson(successResponse);
    }

    @DeleteMapping(path = "blacklist")
    public String deleteFromBlackList(@RequestBody RequestNumberList phoneNumbers) {
        Long status = notificationService.WhiteListNumbers(phoneNumbers);
        SuccessResponse<String> successResponse = new SuccessResponse<>();
        successResponse.setData("Successfully Whitelisted");
        return new Gson().toJson(successResponse);
    }

    @GetMapping(path = "sms/{request_id}")
    public String getSMSById(@PathVariable("request_id") Long requestId) {
        SuccessResponse<Message> successResponse = new SuccessResponse<>();
        Optional<Message> msgById = notificationService.getMessageById(requestId);

        if(msgById.isPresent()) {
            successResponse.setData(msgById.get());
            return new Gson().toJson(successResponse);
        }

        return new Gson().toJson(getResponseErrorObjectFailureResponse(INVALID_REQUEST));
    }

    @GetMapping(path = "contains/{page}/{size}")
    public Page<SendSMSDetails> getSMSDetailsContaining(@PathVariable Integer page, @PathVariable Integer size, @RequestParam String text) {
        return notificationService.getSendSMSDetailsContainingText(page, size, text);
    }

    @GetMapping(path = "between/{page}/{size}")
    public Page<SendSMSDetails> getSMSDetailsBetween(@PathVariable Integer page, @PathVariable Integer size, @RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        return notificationService.getSendSMSDetailsBetween(page, size, from, to);
    }

    @GetMapping(path = "all/{page}/{size}")
    public Page<SendSMSDetails> getAllSMSDetails(@PathVariable int page, @PathVariable int size) {
        return notificationService.getAllSMSDetails(page, size);
    }
}

