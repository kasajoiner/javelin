package javelin.service;

import javelin.AbstractITTest;
import javelin.client.OrderClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class OrderManagerITTest extends AbstractITTest {

    @MockBean
    private OrderClient orderClient;

    @SpyBean
    private TimeService timeService;

    @Autowired
    private OrderManager mngr;

    @BeforeEach
    void isOpen() {
        Mockito.doReturn(true).when(timeService).isOpenNow();
    }

//    @Test
    void When() {
//        Mockito.when(orderClient.findOrders(any(), any())).thenReturn()

        mngr.checkOrders();
    }
}