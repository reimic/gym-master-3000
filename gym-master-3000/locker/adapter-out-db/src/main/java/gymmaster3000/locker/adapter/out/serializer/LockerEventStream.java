package gymmaster3000.locker.adapter.out.serializer;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "lockerId")
public class LockerEventStream {

    @Id
    private UUID lockerId;
    @Version
    private Long version;
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "fk_stream")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<LockerEvent> events;

}
