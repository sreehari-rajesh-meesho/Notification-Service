package com.example.notificationservice.controller;

import com.example.notificationservice.elasticsearch.SendSMSDetails;
import com.example.notificationservice.message.Message;
import com.example.notificationservice.notificationservice.NotificationService;
import com.example.notificationservice.utils.RequestNumberList;
import com.example.notificationservice.utils.*;

import com.meesho.instrumentation.annotation.DigestLogger;
import com.meesho.instrumentation.enums.MetricType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.notificationservice.utils.Constants.*;


@RestController
@RequestMapping("v1")
@AllArgsConstructor
public class Controller {

    private NotificationService notificationService;

    private static ResponseErrorObject getResponseErrorObjectFailureResponse(Long status) {
        ResponseErrorObject error = null;
        if(status == PHONE_NUMBER_MANDATORY) {
            error = new ResponseErrorObject("INVALID_REQUEST", "Phone Number Mandatory");
        }
        else if(status == INVALID_REQUEST) {
            error = new ResponseErrorObject("INVALID_REQUEST", "Invalid request");
        }
        else if(status == DATABASE_ERROR) {
            error = new ResponseErrorObject("REQUEST_FAILED", "Database Error");
        }
        else if(status == MESSAGE_WITH_ID_NOT_FOUND) {
            error = new ResponseErrorObject("REQUEST_FAILED", "Message With Id Not Found");
        }
        return error;
    }

    @DigestLogger(metricType = MetricType.HTTP, tagSet = "api=v1/sms/send")
    @PostMapping(path = "sms/send")
    public ResponseEntity<Response<ResponseDataObject, ResponseErrorObject>> sendSMS(@RequestBody SMSRequest smsRequest) {

        Message message = new Message();
        message.setPhoneNumber(smsRequest.getPhoneNumber());
        message.setMessage(smsRequest.getMessage());

        Long messageId = notificationService.MessageIngestionPhase(message);
        Response<ResponseDataObject, ResponseErrorObject> response = new Response<>();

        if(messageId < 0) {
            response.setError(getResponseErrorObjectFailureResponse(messageId));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ResponseDataObject data = new ResponseDataObject(messageId, "Pending");
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DigestLogger(metricType = MetricType.HTTP, tagSet = "api=v1/blacklist")
    @PostMapping(path = "blacklist")
    public ResponseEntity<Response<String, String>> blackListNumber(@RequestBody RequestNumberList phoneNumbers) {
        Long status = notificationService.BlackListNumbers(phoneNumbers);
        Response<String, String> response = new Response<>();
        if(status == REDIS_ERROR) {
            response.setError("Redis Error");
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
        response.setData("Successfully Blacklisted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DigestLogger(metricType = MetricType.HTTP, tagSet = "api=v1/blacklist")
    @DeleteMapping(path = "blacklist")
    public ResponseEntity<Response<String, String>> deleteFromBlackList(@RequestBody RequestNumberList phoneNumbers) {
        Long status = notificationService.WhiteListNumbers(phoneNumbers);
        Response<String, String> response = new Response<>();
        if(status == REDIS_ERROR){
            response.setError("Redis Error");
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
        response.setData("Successfully Whitelisted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DigestLogger(metricType = MetricType.HTTP, tagSet = "api=v1/sms")
    @GetMapping(path = "sms/{request_id}")
    public ResponseEntity<Response<Message, ResponseErrorObject>> getSMSById(@PathVariable("request_id") Long requestId) {

        Response<Message, ResponseErrorObject> response = new Response<>();
        Optional<Message> msgById = notificationService.getMessageById(requestId);

        if(msgById.isPresent()) {
            response.setData(msgById.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ResponseErrorObject errorObject = getResponseErrorObjectFailureResponse(INVALID_REQUEST);
        response.setError(errorObject);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @DigestLogger(metricType = MetricType.HTTP, tagSet = "api=v1/contains")
    @GetMapping(path = "contains/{page}/{size}")
    public ResponseEntity<PageResponse<SendSMSDetails>> getSMSDetailsContaining(@PathVariable Integer page, @PathVariable Integer size, @RequestParam String text) {
        PageResponse<SendSMSDetails> response = notificationService.getSendSMSDetailsContainingText(page, size, text);
        if(response.getError() == null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @DigestLogger(metricType = MetricType.HTTP, tagSet = "api=v1/between")
    @GetMapping(path = "between/{page}/{size}")
    public ResponseEntity<PageResponse<SendSMSDetails>> getSMSDetailsBetween(@PathVariable Integer page, @PathVariable Integer size, @RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        PageResponse<SendSMSDetails> response = notificationService.getSendSMSDetailsBetween(page, size, from, to);
        if(response.getError() == null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @DigestLogger(metricType = MetricType.HTTP, tagSet = "api=v1/all")
    @GetMapping(path = "all/{page}/{size}")
    public ResponseEntity<PageResponse<SendSMSDetails>> getAllSMSDetails(@PathVariable int page, @PathVariable int size) {
        PageResponse<SendSMSDetails> response = notificationService.getAllSMSDetails(page, size);
        if(response.getError() == null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}

