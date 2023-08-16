package gymmaster3000.member.application.port;

import gymmaster3000.member.domain.valueobject.MemberId;

import java.util.Map;
import java.util.UUID;

public interface FindPreferredLockersPort {

    Map<UUID, Long> findBy(MemberId memberId);

}
