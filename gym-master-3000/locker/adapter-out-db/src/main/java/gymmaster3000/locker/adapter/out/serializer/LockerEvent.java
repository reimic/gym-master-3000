package gymmaster3000.locker.adapter.out.serializer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class LockerEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_event")
    @SequenceGenerator(name = "seq_event", sequenceName = "seq_event")
    private Long id;

    @Lob
    private byte[] content;

}
