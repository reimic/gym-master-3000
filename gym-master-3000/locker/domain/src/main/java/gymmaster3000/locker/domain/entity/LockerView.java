package gymmaster3000.locker.domain.entity;

import java.util.UUID;

public record LockerView(UUID lockerId, UUID currentRenterId) {

    public static LockerView from(Locker locker) {
        return new LockerView(
                locker.getLockerId()
                      .value(),
                locker.getCurrentRenterId()
                      .isPresent() ? locker.getCurrentRenterId()
                                           .get()
                                           .value() : null);
    }

    public String toJSON() {
        return "{\"lockerId\":\"" + lockerId + "\",\"currentRenterId\":\"" + currentRenterId + "\"}";
    }

}
