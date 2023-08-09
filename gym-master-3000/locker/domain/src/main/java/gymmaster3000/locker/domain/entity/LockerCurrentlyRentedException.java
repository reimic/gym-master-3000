package gymmaster3000.locker.domain.entity;

import java.util.UUID;

public class LockerCurrentlyRentedException extends RuntimeException {

    public static final String LOCKER_CURRENTLY_RENTED = "Locker with id=%s is currently rented.";

    public LockerCurrentlyRentedException(UUID lockerId) {
        super(LOCKER_CURRENTLY_RENTED.formatted(lockerId));
    }

}
