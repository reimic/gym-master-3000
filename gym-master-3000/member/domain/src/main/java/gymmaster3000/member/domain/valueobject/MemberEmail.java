package gymmaster3000.member.domain.valueobject;

import java.io.Serializable;
import java.util.regex.Pattern;

@SelfValidating
public class MemberEmail implements ValueObject<String>, Serializable {

    private static final Pattern PATTERN_EMAIL = Pattern.compile("^(.+)@(\\S+)");
    public static final String INVALID_EMAIL = "Provided memberEmail=%s is invalid.";

    private final String value;

    private MemberEmail(final String value) {
        this.value = value;
    }

    public static MemberEmail of(String value) {
        if (value == null || !PATTERN_EMAIL.matcher(value)
                                           .matches()) {
            throw new IllegalArgumentException(INVALID_EMAIL.formatted(value));
        }
        return new MemberEmail(value);
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberEmail other)) {
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
