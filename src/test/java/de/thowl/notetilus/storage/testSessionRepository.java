package de.thowl.notetilus.storage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.thowl.notetilus.storage.entities.Session;
import de.thowl.notetilus.storage.entities.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class TestSessionRepository {

    @Autowired
    private SessionRepository sessions;

    @Test
    void testStoreSession() {
        log.debug("entering testStoreSession()");
        final String sessionId = "1234567890";
        User usr = new User();
        usr.setId(1);
        usr.setUsername("Keres");
        usr.setEmail("godlike@thyart.web");
        usr.setPassword("joinM3In4rmageddon");
        Session session = new Session(sessionId, usr);

        this.sessions.save(session);
        assertNotNull(this.sessions.findByAuthToken(sessionId));
    }

}
