package javelin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateOrderRequest(
    @JsonProperty("spot_id") int spotId,
    String phone,
    @JsonProperty("service_mode") int service,
    List<Product> products
) {

    public record Product(
//        @JsonProperty("product_id") int productId,
        @JsonProperty("modificator_id") int modificatorId,
        int count) {
    }
}
