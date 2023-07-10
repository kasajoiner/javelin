package javelin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "client")
@Data
public class Client {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private String phone;
}
