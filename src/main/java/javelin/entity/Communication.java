package javelin.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "communication")
public class Communication extends DatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String txt;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String objectId;
    private String objectUrl;
    private Long sender;
    @Enumerated(EnumType.STRING)
    private Receiver receiver;

    public enum Type {
        TEXT, PHOTO, VIDEO
    }

    public enum Status {
        CREATED, ACCEPTED, CANCELLED
    }
}
