package javelin.client.poster;

import javelin.client.StatusClient;
import javelin.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PosterStatusClientTest {

    @Autowired
    private PosterStatusClient client;

    @Test
    void When_GetStatusById_ExpectCorrectStatus() {

        var status = client.getStatus(11L);

        assertTrue(status.isPresent());
        assertEquals(Order.Status.CLOSED, status.get());
    }

    @Test
    void When_GetStatusByNotExistingId_ExpectEmptyStatus() {

        var status = client.getStatus(-1L);

        assertFalse(status.isPresent());
    }
}