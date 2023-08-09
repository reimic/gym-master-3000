package gymmaster3000.member.application.port;

import gymmaster3000.locker.domain.entity.Locker;

public interface SaveLockerPort {

    void save(Locker locker);

}
