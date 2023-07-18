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
    void When_CreateOrder_Expect_CorrectParameters() {
        var request = new CreateOrderRequest(
            1,
            "+380955741012",
            3,
            List.of(
                new CreateOrderRequest.Product(
                    3, 2
                )
            )
        );

        var incomingOrder = orderClient.create(request);

        assertEquals("+380955741012", incomingOrder.phone());
        assertEquals(0, incomingOrder.status());
        assertEquals(0, incomingOrder.serviceMode());
    }

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