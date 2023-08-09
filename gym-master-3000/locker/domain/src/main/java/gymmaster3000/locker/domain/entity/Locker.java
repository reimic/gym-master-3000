package gymmaster3000.locker.domain.entity;

import gymmaster3000.locker.domain.event.IncomingEvent;
import gymmaster3000.locker.domain.event.ReleaseLockerEvent;
import gymmaster3000.locker.domain.event.RentLockerEvent;
import gymmaster3000.locker.domain.event.SetupLockerEvent;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EqualsAndHashCode(of = "lockerId")
public class Locker {

    private int sequenceNumer;
    private List<IncomingEvent> pendingEvents = new ArrayList<>();
    @Getter
    private LockerId lockerId;
    @Getter
    private Long version;

    private RenterId currentRenterId;

    private Locker(Long version, int sequenceNumer) {
        this.version = version;
        this.sequenceNumer = sequenceNumer;
    }

    private Locker() {
        var setup = SetupLockerEvent.builder()
                                    .createDate(LocalDateTime.now())
                                    .sequenceNumber(sequenceNumer++)
                                    .lockerId(LockerId.of(UUID.randomUUID()))
                                    .build();
        handleWithAppend(setup);
    }

    public static Locker create() {
        return new Locker();
    }

    public static Locker recreate(List<IncomingEvent> events, Long version) {
        var locker = new Locker(version, events.size());
        events.forEach(locker::handleDispatcher);
        return locker;
    }

    private void handle(SetupLockerEvent setup) {
        this.lockerId = setup.getLockerId();
    }

    public void rent(RenterId renterId) {
        if (currentRenterId != null) {
            throw new LockerCurrentlyRentedException(lockerId.value());
        }
        var rent = RentLockerEvent.builder()
                                  .createDate(LocalDateTime.now())
                                  .sequenceNumber(sequenceNumer++)
                                  .renterId(renterId)
                                  .build();
        handleWithAppend(rent);
    }

    private void handle(RentLockerEvent rent) {
        this.currentRenterId = rent.getRenterId();
    }

    public void release() {
        // TODO - Consider logging if locker was empty, but was commanded to release itself
        var release = ReleaseLockerEvent.builder()
                                        .createDate(LocalDateTime.now())
                                        .sequenceNumber(sequenceNumer++)
                                        .build();
        handleWithAppend(release);
    }

    private void handle(ReleaseLockerEvent release) {
        this.currentRenterId = null;
    }

    public Optional<RenterId> getCurrentRenterId() {
        if (currentRenterId == null) {
            return Optional.empty();
        }
        return Optional.of(currentRenterId);
    }

    public boolean isRented() {
        return currentRenterId != null;
    }

    public boolean isAvailable() {
        return currentRenterId == null;
    }

    public boolean isRentedBy(RenterId renterId) {
        if (renterId == null) {
            return false;
        }
        return renterId.equals(currentRenterId);
    }

    private void handleWithAppend(IncomingEvent event) {
        pendingEvents.add(event);
        handleDispatcher(event);
    }

    private void handleDispatcher(IncomingEvent event) {
        switch (event) {
            case SetupLockerEvent setup -> handle(setup);
            case RentLockerEvent rent -> handle(rent);
            case ReleaseLockerEvent release -> handle(release);
            default -> throw new IllegalArgumentException();
        }
    }

    public List<IncomingEvent> getAndClearPendingEvents() {
        var events = pendingEvents;
        pendingEvents = new ArrayList<>();
        return events;
    }

}
