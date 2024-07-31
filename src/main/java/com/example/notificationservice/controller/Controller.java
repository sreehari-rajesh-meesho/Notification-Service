package com.example.notificationservice.controller;


import com.example.notificationservice.elasticsearch.SendSMSDetails;
import com.example.notificationservice.message.Message;
import com.example.notificationservice.notificationservice.NotificationService;
import com.example.notificationservice.utils.RequestNumberList;
import com.example.notificationservice.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    public Response<ResponseDataObject, ResponseErrorObject> sendSMS(@RequestBody SMSRequest smsRequest) {

            Message message = new Message();
            message.setPhone_number(smsRequest.getPhoneNumber());
            message.setMessage(smsRequest.getMessage());
            Long messageId = notificationService.MessageIngestionPhase(message);
            Response<ResponseDataObject, ResponseErrorObject> response = new Response<>();

            if(messageId < 0) {
                 response.setError(getResponseErrorObjectFailureResponse(messageId).getError());
            }
            ResponseDataObject data = new ResponseDataObject(messageId, "Pending");
            response.setData(data);
            return response;
    }

    @PostMapping(path = "blacklist")
    public Response<String, String> blackListNumber(@RequestBody RequestNumberList phoneNumbers) {
        Long status = notificationService.BlackListNumbers(phoneNumbers);
        Response<String, String> successResponse = new Response<>();
        successResponse.setData("Successfully Blacklisted");
        return successResponse;
    }

    @DeleteMapping(path = "blacklist")
    public Response<String, String> deleteFromBlackList(@RequestBody RequestNumberList phoneNumbers) {
        Long status = notificationService.WhiteListNumbers(phoneNumbers);
        Response<String, String> successResponse = new Response<>();
        successResponse.setData("Successfully Whitelisted");
        return successResponse;
    }

    @GetMapping(path = "sms/{request_id}")
    public Response<Message, ResponseErrorObject> getSMSById(@PathVariable("request_id") Long requestId) {

        Response<Message, ResponseErrorObject> response = new Response<>();
        Optional<Message> msgById = notificationService.getMessageById(requestId);

        if(msgById.isPresent()) {
            response.setData(msgById.get());
            return response;
        }

        ResponseErrorObject errorObject = getResponseErrorObjectFailureResponse(INVALID_REQUEST).getError();
        response.setError(errorObject);
        return response;
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
