package gymmaster3000.locker.domain.entity;

import java.util.UUID;

public class LockerDispatchedNotImplementedEventException extends RuntimeException {

    public static final String DISPATCHED_NOT_IMPLEMENTED_EVENT = "Locker with id=%s dispatched a not implemented event=%s.";

    public LockerDispatchedNotImplementedEventException(UUID lockerId, String event) {
        super(DISPATCHED_NOT_IMPLEMENTED_EVENT.formatted(lockerId, event));
    }

}
