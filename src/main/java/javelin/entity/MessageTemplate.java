package javelin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "msg_template")
public class MessageTemplate extends DatedEntity {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String txt;
}
