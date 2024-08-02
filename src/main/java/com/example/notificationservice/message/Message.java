package com.example.notificationservice.message;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@ToString
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
    private String phoneNumber;
    private String message;
    private Integer status;
    private String failureCode;
    private String failureComments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

