package gymmaster3000.member.domain.valueobject;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@RequiredArgsConstructor(staticName = "of")
public record MemberId(UUID value) implements ValueObject<UUID>, Serializable {

}
