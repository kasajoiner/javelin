package javelin.client.poster;

import javelin.dto.CreateOrderRequest;
import javelin.dto.IncomingOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PosterMenuClientTest {

    @Autowired
    private PosterOrderClient orderClient;

    @Test
    void When_GetOrders_Expect_Them_NotEmpty() {
        var start = LocalDateTime.of(2023, 7, 1, 0, 0, 0);
        var to = LocalDateTime.of(2023, 7, 30, 0, 0, 0);
        var categories = orderClient.findOrders(
            start, to
        );

        assertTrue(categories.size() > 0);
    }

}