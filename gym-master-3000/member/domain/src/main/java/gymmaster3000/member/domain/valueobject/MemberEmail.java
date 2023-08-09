package gymmaster3000.member.domain.valueobject;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;
@RequiredArgsConstructor(staticName = "of")
public record MemberEmail(String value) implements ValueObject<String>, Serializable {

    //TODO validate email string with regex

}
