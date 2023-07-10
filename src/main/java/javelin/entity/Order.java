package javelin.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "orders")
public class Order {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private String address;
    private Status status;
    private LocalDateTime created;
    private LocalDateTime updated;

    @Getter
    @AllArgsConstructor
    public enum Status {
        NEW(0), ACCEPTED(1), CANCELLED(7);
        private final int id;
    }
}
