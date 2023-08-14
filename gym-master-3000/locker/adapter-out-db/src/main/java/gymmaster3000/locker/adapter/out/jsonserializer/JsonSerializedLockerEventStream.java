package gymmaster3000.locker.adapter.out.jsonserializer;

import gymmaster3000.locker.adapter.out.db.LockerEventStream;
import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import gymmaster3000.locker.adapter.out.javaserializer.JavaSerializedLockerEvent;
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
@Profile(SerializerProfile.JSON_SERIALIZER)
@RequiredArgsConstructor
public class JsonSerializedLockerEventStream implements LockerEventStream<String, JsonSerializedLockerEvent> {

    @Id
    private UUID lockerId;
    @Version
    private long version;
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "fk_stream")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<JsonSerializedLockerEvent> events;

    public JsonSerializedLockerEventStream(UUID lockerId) {
        this.lockerId = lockerId;
        this.events = new HashSet<>();
    }

}
