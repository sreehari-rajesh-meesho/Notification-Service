package com.example.notificationservice.elasticsearch;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "message")
public class SendSMSDetails {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "phone_number")
    private String phoneNumber;

    @Field(type = FieldType.Text, name = "message")
    private String message;

    @Field(type = FieldType.Integer, name = "status")
    private Integer status;

    @Field(type = FieldType.Text, name = "failure_code")
    private String failureCode;

    @Field(type = FieldType.Text, name="failure_comments")
    private String failureComments;

    @Field(type=FieldType.Date, name="created_at")
    private Long created;

    @Field(type = FieldType.Date, name = "updated_at")
    private Long updated;
}

