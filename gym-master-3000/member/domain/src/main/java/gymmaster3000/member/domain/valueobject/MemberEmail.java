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

}
