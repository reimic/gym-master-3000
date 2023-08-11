package gymmaster3000.locker.adapter.out.db;

import gymmaster3000.locker.application.port.FindAllLockersPort;
import gymmaster3000.locker.application.port.FindLockerPort;
import gymmaster3000.locker.application.port.SaveAllLockersPort;
import gymmaster3000.locker.application.port.SaveLockerPort;

public interface LockerEventStreamPort extends SaveLockerPort,
                                               SaveAllLockersPort,
                                               FindLockerPort,
                                               FindAllLockersPort {

}
