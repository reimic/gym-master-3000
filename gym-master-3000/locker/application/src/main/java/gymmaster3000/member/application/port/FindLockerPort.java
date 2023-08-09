package gymmaster3000.member.application.port;

import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.locker.domain.valueobject.LockerId;

import java.util.Optional;

public interface FindLockerPort {

    Optional<Locker> findBy(LockerId lockerId);

}
