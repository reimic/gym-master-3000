package gymmaster3000.member.domain.valueobject;

import java.io.Serializable;

public record MemberEmail(String value) implements ValueObject<String>, Serializable {

    //TODO validate email string with regex

}
