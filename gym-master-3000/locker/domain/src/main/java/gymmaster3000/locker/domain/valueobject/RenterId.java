package gymmaster3000.locker.domain.valueobject;

import java.io.Serializable;
import java.util.UUID;

public record RenterId(UUID value) implements ValueObject<UUID>, Serializable {

    public static RenterId of(UUID value) {
        return new RenterId(value);
    }

}
