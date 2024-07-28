package com.example.notificationservice.elasticsearch;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@AllArgsConstructor
@Data
@Document(indexName = "message")
public class SendSMSDetails {

    @Id
    private Long id;

    @Field(type = FieldType.Long, name = "phone_number")
    private String phone_number;

    @Field(type = FieldType.Text, name = "message")
    private String message;

    @Field(type = FieldType.Integer, name = "status")
    private Integer status;

    @Field(type = FieldType.Text, name = "failure_code")
    private String failure_code;

    @Field(type = FieldType.Text, name="failure_comments")
    private String failure_comments;

    @Field(type=FieldType.Date, name="created_at")
    private String created_at;

    @Field(type = FieldType.Date, name = "updated_at")
    private String updated_at;
}

