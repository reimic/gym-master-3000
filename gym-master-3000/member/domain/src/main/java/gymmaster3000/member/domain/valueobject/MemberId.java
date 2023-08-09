package gymmaster3000.member.domain.valueobject;

import java.io.Serializable;
import java.util.UUID;

public record MemberId(UUID value) implements ValueObject<UUID>, Serializable {

    public static MemberId of(UUID value) {
        return new MemberId(value);
    }

}
