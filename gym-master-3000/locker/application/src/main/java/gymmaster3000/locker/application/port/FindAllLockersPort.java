package gymmaster3000.locker.application.port;

import gymmaster3000.locker.domain.entity.Locker;

import java.util.List;

public interface FindAllLockersPort {

    List<Locker> findAll();

}
