package javelin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Entity(name = "orders")
public class Order {

    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private Long clientId;
    private String address;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Service service;
    private Integer price;
    private LocalDateTime created;
    private LocalDateTime updated;

    public boolean isFinished() {
        return status == Status.OUT
            || status == Status.DONE
            || status == Status.CANCELLED
            || status == Status.DELETED;
    }

    @Getter
    @AllArgsConstructor
    public enum Status {
        NEW,
        ACCEPTED,
        COOKING,
        COOKED,
        DELIVERING,
        DELIVERED,
        DONE,
        OUT,
        CANCELLED,
        DELETED
    }

    public enum Service {
        DINEIN, DELIVERY
    }
}
