package javelin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

//@Data
//@Entity(name = "customer")
public class Customer {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ENABLED;

    public enum Status {
        ENABLED, DISABLED
    }
}
