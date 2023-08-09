package gymmaster3000.locker.domain.entity;

import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;

public record LockerView(LockerId lockerId, RenterId currentRenterId) {

    public static LockerView from(Locker locker) {
        return new LockerView(
                locker.getLockerId(),
                locker.getCurrentRenterId()
                      .orElse(null));
    }

}
