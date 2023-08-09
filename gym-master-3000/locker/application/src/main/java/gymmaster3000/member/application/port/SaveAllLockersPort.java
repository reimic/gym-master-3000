package gymmaster3000.member.application.port;

import gymmaster3000.locker.domain.entity.Locker;

import java.util.List;

public interface SaveAllLockersPort {

    void saveAll(List<Locker> lockers);

}
