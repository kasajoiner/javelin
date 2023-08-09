package javelin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity(name = "client")
@Data
public class Client {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private String phone;
    private String tag;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ENABLED;

    public enum Status {
        ENABLED, DISABLED
    }
}
