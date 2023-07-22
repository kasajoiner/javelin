package javelin.dto;

import javelin.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientOrder {
    private Long id;
    private Long transactionId;
    private String phone;
    private String address;
    private Order.Service service;
    private Order.Status status;
    private Integer price;
    private LocalDateTime created;
    private LocalDateTime updated;
}
