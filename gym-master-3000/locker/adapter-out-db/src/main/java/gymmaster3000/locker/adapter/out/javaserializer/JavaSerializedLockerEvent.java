package gymmaster3000.locker.adapter.out.javaserializer;

import gymmaster3000.locker.adapter.out.db.LockerEvent;
import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Profile;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "eventId", callSuper = false)
@Profile(SerializerProfile.JAVA_SERIALIZER)
@RequiredArgsConstructor
public class JavaSerializedLockerEvent implements LockerEvent<byte[]> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_event")
    @SequenceGenerator(name = "seq_event", sequenceName = "seq_event")
    private long eventId;

    @Lob
    private byte[] content;

}
