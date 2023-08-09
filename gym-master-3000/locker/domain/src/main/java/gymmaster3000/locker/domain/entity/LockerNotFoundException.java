package gymmaster3000.locker.domain.entity;

import java.util.UUID;

public class LockerNotFoundException extends RuntimeException {

    public static final String LOCKER_NOT_FOUND = "No locker with id=%s was found.";

    public LockerNotFoundException(UUID lockerId) {
        super(LOCKER_NOT_FOUND.formatted(lockerId));
    }

}
