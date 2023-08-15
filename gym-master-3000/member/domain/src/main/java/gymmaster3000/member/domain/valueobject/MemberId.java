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

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberId other)) {
            return false;
        }
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.value.hashCode();
        return result;
    }

}