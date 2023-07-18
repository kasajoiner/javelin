package javelin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IncomingOrder(
    @JsonProperty("incoming_order_id") Long id,
    @JsonProperty("status") Integer status,
    @JsonProperty("first_name") String firstName,
    @JsonProperty("last_name") String lastName,
    @JsonProperty("phone") String phone,
    @JsonProperty("email") String email,
    @JsonProperty("transaction_id") Long transactionId,
    @JsonProperty("birthday") String birthday,
    @JsonProperty("address") String address,
    @JsonProperty("comment") String comment,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at") LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updated_at") LocalDateTime updatedAt,
    @JsonProperty("service_mode") Integer serviceMode,
    @JsonProperty("products") List<Product> products
) {
    public record Product(
        @JsonProperty("io_product_id") Long ioProductId,
        @JsonProperty("product_id") Long productId,
        @JsonProperty("price") Integer price,
        @JsonProperty("count") Double count
    ) {
    }

    public Integer countPrice() {
        double sum = products.stream()
            .mapToDouble(product -> product.price() * product.count())
            .sum();
        return (int) Math.round(sum);
    }

}
