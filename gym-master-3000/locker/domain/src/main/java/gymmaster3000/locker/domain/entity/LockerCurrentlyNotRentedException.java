package gymmaster3000.locker.domain.entity;

import java.util.UUID;

public class LockerCurrentlyNotRentedException extends RuntimeException {

    public static final String LOCKER_CURRENTLY_NOT_RENTED = "Locker with id=%s is currently not rented.";

    public LockerCurrentlyNotRentedException(UUID lockerId) {
        super(LOCKER_CURRENTLY_NOT_RENTED.formatted(lockerId));
    }

}
