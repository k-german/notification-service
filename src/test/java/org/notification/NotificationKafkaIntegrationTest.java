package org.notification;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
        topics = "user.notifications",
        partitions = 1
)
class NotificationKafkaIntegrationTest {

    @Test
    void contextStarts_withEmbeddedKafka() {

    }
}
