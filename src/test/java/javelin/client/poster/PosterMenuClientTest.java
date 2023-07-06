package javelin.client.poster;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PosterMenuClientTest {

    @Autowired
    private PosterMenuClient menuClient;

    @Test
    void When_GetCategories_Expect_Them_NotEmpty() {
        var categories = menuClient.getCategories();

        assertTrue(categories.size() > 0);
    }

    @Test
    void When_GetProducts_Expect_Them_NotEmpty() {
        var products = menuClient.getProducts();

        assertTrue(products.size() > 0);
    }

}