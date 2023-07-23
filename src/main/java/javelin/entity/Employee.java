package javelin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@Entity(name = "employee")
public class Employee {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ENABLED;
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Status {
        ENABLED, DISABLED
    }

    public enum Role {
        BOSS, DEV, ADMIN
    }
}
