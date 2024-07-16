package com.example.notificationservice.message;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import java.time.LocalDateTime;

public class Message {

    @Id
    @SequenceGenerator(
            name = "message_sequence",
            sequenceName = "message_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "message_sequence"
    )
    private Long id;
    private Integer phone_number;
    private String message;
    private Integer status;
    private String failure_code;
    private String failure_comments;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Message(Integer phone_number, String message, Integer status, String failure_code, String failure_comments, LocalDateTime created_at, LocalDateTime updated_at) {
        this.phone_number = phone_number;
        this.message = message;
        this.status = status;
        this.failure_code = failure_code;
        this.failure_comments = failure_comments;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFailure_code() {
        return failure_code;
    }

    public void setFailure_code(String failure_code) {
        this.failure_code = failure_code;
    }

    public String getFailure_comments() {
        return failure_comments;
    }

    public void setFailure_comments(String failure_comments) {
        this.failure_comments = failure_comments;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}

