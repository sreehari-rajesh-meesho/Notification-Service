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
    private String phone_number;
    private String message;
    private Integer status;
    private String failure_code;
    private String failure_comments;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}

