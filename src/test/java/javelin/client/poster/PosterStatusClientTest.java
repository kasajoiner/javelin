package javelin.client.poster;

import javelin.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PosterStatusClientTest {

    @Autowired
    private PosterStatusClient client;

    @Test
    void When_GetStatusById_ExpectCorrectStatus() {

        var status = client.getStatus(11L);

        assertTrue(status.isPresent());
        assertEquals(Order.Status.DONE, status.get());
    }

    @Test
    void When_GetStatusByNotExistingId_ExpectEmptyStatus() {

        var status = client.getStatus(-1L);

        assertFalse(status.isPresent());
    }
}