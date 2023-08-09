package gymmaster3000.locker.domain.valueobject;

import java.io.Serializable;
import java.util.UUID;

public record LockerId(UUID value) implements ValueObject<UUID>, Serializable {

    public static LockerId of(UUID value) {
        return new LockerId(value);
    }

}
