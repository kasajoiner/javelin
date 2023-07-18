package javelin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
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
    private Status status;
    private Integer price;
    private LocalDateTime created;
    private LocalDateTime updated;

    @Getter
    @AllArgsConstructor
    public enum Status {
        NEW(0), ACCEPTED(1), CANCELLED(7), COOKED(30), DELIVERING(40), DONE(50);
        private final int id;

        private static final Map<Integer, Status> ID_TO_ENTITY = Arrays.stream(values())
            .collect(Collectors.toMap(Status::getId, Function.identity()));

        public static Status of(int id) {
            return ID_TO_ENTITY.get(id);
        }
    }
}
