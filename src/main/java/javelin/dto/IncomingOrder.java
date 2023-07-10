package javelin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record IncomingOrder(
    @JsonProperty("incoming_order_id") Long incomingOrderId,
    @JsonProperty("status") Integer status,
    @JsonProperty("client_id") Long clientId,
    @JsonProperty("first_name") String firstName,
    @JsonProperty("last_name") String lastName,
    @JsonProperty("phone") String phone,
    @JsonProperty("email") String email,
    @JsonProperty("sex") Integer sex,
    @JsonProperty("birthday") String birthday,
    @JsonProperty("address") String address,
    @JsonProperty("comment") String comment,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at") LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updated_at") LocalDateTime updatedAt,
    @JsonProperty("service_mode") Integer serviceMode
) {
}
