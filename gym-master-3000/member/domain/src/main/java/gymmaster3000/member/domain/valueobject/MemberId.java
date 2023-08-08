package gymmaster3000.member.domain.valueobject;

import java.io.Serializable;
import java.util.UUID;

public record MemberId(UUID value) implements ValueObject<UUID>, Serializable {

}
