package gymmaster3000.member.domain.valueobject;

import java.io.Serializable;
import java.util.UUID;

@SelfValidating
public class MemberId implements ValueObject<UUID>, Serializable {

    public static final String INVALID_ID = "Provided memberId=%s is invalid.";
    private final UUID value;

    private MemberId(UUID value) {
        this.value = value;
    }

    public static MemberId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException(INVALID_ID.formatted("null"));
        }
        return new MemberId(value);
    }

    @Override
    public UUID value() {
        return this.value;
    }

}