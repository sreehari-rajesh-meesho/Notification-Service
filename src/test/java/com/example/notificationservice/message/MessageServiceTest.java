package com.example.notificationservice.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.notificationservice.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    // Best Case Scenario;
    @Test
    public void TestIngestMessageToDatabaseI() {

            Message message = new Message();
            message.setPhone_number("+912781781787");
            message.setMessage("This is a test message");

            Long testMessageId = 10L;

            // Normal Insertion
            Message messageById = new Message();
            messageById.setPhone_number("+912781781787");
            messageById.setMessage("This is a test message");
            messageById.setId(testMessageId);
            Mockito.when(messageRepository.save(message)).thenReturn(messageById);
            Long messageId = messageService.IngestMessageToDatabase(message);
            assertEquals(messageId, testMessageId);

            // DB error
            Message messageDBError = new Message();
            messageDBError.setPhone_number("+912781781787");
            messageDBError.setMessage("This is a test message");
            Mockito.when(messageRepository.save(messageDBError)).thenThrow(new RuntimeException());
            Long messageDBErrorId = messageService.IngestMessageToDatabase(messageDBError);
            assertEquals(messageDBErrorId, DATABASE_ERROR);
    }

    @Test
    public void TestFindMessageById() {

            Message messageById = new Message();
            messageById.setPhone_number("+912781781787");
            messageById.setMessage("This is a test message");
            Long testMessageId = 10L;
            messageById.setId(testMessageId);

            Optional<Message> returnValue = Optional.of(messageById);
            Mockito.when(messageRepository.findById(testMessageId)).thenReturn(returnValue);
            Optional<Message> message = messageService.findMessageById(testMessageId);
            assertEquals(message.get(), messageById);

            Optional<Message> emptyMessage = Optional.empty();
            Mockito.when(messageRepository.findById(testMessageId)).thenReturn(emptyMessage);
            Optional<Message> nullMessage = messageService.findMessageById(testMessageId);
            assertFalse(nullMessage.isPresent());
    }

    @Test
    public void UpdateMessageInDatabase() {
            Message messageToUpdate = new Message();
            messageToUpdate.setPhone_number("+912781781787");
            messageToUpdate.setMessage("This is a test message");
            Long testMessageId = 10L;
            messageToUpdate.setId(testMessageId);
            // Update
            Mockito.when(messageRepository.findById(testMessageId)).thenReturn(Optional.of(messageToUpdate));
            Mockito.when(messageRepository.save(messageToUpdate)).thenReturn(messageToUpdate);
            Long messageId = messageService.UpdateMessageInDatabase(testMessageId, 404, "", "");
            assertEquals(messageId, testMessageId);

            // Message With ID not found;
            Mockito.when(messageRepository.findById(testMessageId)).thenReturn(Optional.empty());
            Long emptyMessageId = messageService.UpdateMessageInDatabase(testMessageId, 404, "", "");
            assertEquals(emptyMessageId, MESSAGE_WITH_ID_NOT_FOUND);

            // Database Exception;
            Mockito.when(messageRepository.findById(testMessageId)).thenReturn(Optional.of(messageToUpdate));
            Mockito.when(messageRepository.save(messageToUpdate)).thenThrow(new RuntimeException());
            Long dbErrorId = messageService.UpdateMessageInDatabase(testMessageId, 404, "", "");
            assertEquals(dbErrorId, DATABASE_ERROR);
    }
}
