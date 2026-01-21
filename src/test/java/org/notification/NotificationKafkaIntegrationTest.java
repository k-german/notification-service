package org.notification;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import org.notification.kafka.dto.OperationType;
import org.notification.kafka.dto.UserNotificationEvent;

import jakarta.mail.internet.MimeMessage;

import jakarta.mail.internet.MimeMessage;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
        topics = "user.notifications",
        partitions = 1
)
class NotificationKafkaIntegrationTest {

    @Autowired
    KafkaTemplate<String, org.notification.kafka.dto.UserNotificationEvent> kafkaTemplate;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    @Test
    void contextStarts_withEmbeddedKafka() {
    }

    @Test
    void shouldConsumeKafkaEvent_andSendEmail() throws Exception {
        UserNotificationEvent event = new UserNotificationEvent(OperationType.CREATE, "test@mail.com");

        kafkaTemplate.send("user.notifications", event);
        greenMail.waitForIncomingEmail(1);
        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("test@mail.com", messages[0].getAllRecipients()[0].toString());
    }

    @Test
    void shouldSendCreateEmail_withExpectedSubjectAndBody() throws Exception {
        UserNotificationEvent event =
                new UserNotificationEvent(OperationType.CREATE, "test@mail.com");

        kafkaTemplate.send("user.notifications", event);

        greenMail.waitForIncomingEmail(1);
        MimeMessage message = greenMail.getReceivedMessages()[0];

        String subject = message.getSubject();
        String body = message.getContent().toString();

        assertTrue(subject.contains("Test message"), "Subject must indicate Test message");
        assertTrue(body.contains("SUCCESSFULLY CREATED"), "Body must indicate CREATE");
    }

    @Test
    void shouldSendDeleteEmail_withExpectedSubjectAndBody() throws Exception {
        UserNotificationEvent event =
                new UserNotificationEvent(OperationType.DELETE, "test@mail.com");

        kafkaTemplate.send("user.notifications", event);

        greenMail.waitForIncomingEmail(1);
        MimeMessage message = greenMail.getReceivedMessages()[0];

        String subject = message.getSubject();
        String body = message.getContent().toString();

        assertTrue(subject.contains("Test message"), "Subject must indicate Test message");
        assertTrue(body.contains("DELETED"), "Body must indicate DELETED");
    }


}
