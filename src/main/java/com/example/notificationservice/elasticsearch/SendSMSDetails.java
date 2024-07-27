package com.example.notificationservice.elasticsearch;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
@Document(indexName = "message")
public class SendSMSDetails {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword, name = "phone_number")
    private Long phone_number;

    @Field(type = FieldType.Text, name = "message")
    private String message;

    @Field(type = FieldType.Keyword, name = "status")
    private Integer status;

    @Field(type = FieldType.Text, name = "failure_code")
    private String failure_code;

    @Field(type = FieldType.Text, name="failure_comments")
    private String failure_comments;

    @Field(type = FieldType.Text, name="created_at")
    private String created_at;

    @Field(type = FieldType.Text, name="updated_at")
    private String updated_at;
}

