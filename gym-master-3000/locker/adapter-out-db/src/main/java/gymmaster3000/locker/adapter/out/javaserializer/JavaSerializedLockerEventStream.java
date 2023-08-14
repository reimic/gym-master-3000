package gymmaster3000.locker.adapter.out.javaserializer;

import gymmaster3000.locker.adapter.out.db.LockerEventStream;
import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.context.annotation.Profile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode(of = "lockerId", callSuper = false)
@Profile(SerializerProfile.JAVA_SERIALIZER)
@RequiredArgsConstructor
public class JavaSerializedLockerEventStream implements LockerEventStream<byte[], JavaSerializedLockerEvent> {

    @Id
    private UUID lockerId;
    @Version
    private long version;
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "fk_stream")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<JavaSerializedLockerEvent> events;

    public JavaSerializedLockerEventStream(UUID lockerId) {
        this.lockerId = lockerId;
        this.events = new HashSet<>();
    }

}
