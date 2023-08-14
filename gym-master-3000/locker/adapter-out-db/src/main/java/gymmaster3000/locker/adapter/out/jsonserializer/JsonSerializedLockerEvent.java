package gymmaster3000.locker.adapter.out.jsonserializer;

import gymmaster3000.locker.adapter.out.db.LockerEvent;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Profile;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "eventId", callSuper = false)
@Profile("json-serializer")
@RequiredArgsConstructor
public class JsonSerializedLockerEvent implements LockerEvent<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_event")
    @SequenceGenerator(name = "seq_event", sequenceName = "seq_event")
    private long eventId;

    @Lob
    private String content;

}
