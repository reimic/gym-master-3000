package gymmaster3000.locker.domain.event;

public enum LockerEventType {
    SET_UP_LOCKER("SetUpLockerEvent"),
    RENT_LOCKER("RentLockerEvent"),
    RELEASE_LOCKER("ReleaseLockerEvent");
    public final String eventName;

    LockerEventType(final String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return this.eventName;
    }
}
