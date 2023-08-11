package gymmaster3000.locker.domain.entity;

import gymmaster3000.locker.domain.event.IncomingEvent;
import gymmaster3000.locker.domain.event.ReleaseLockerEvent;
import gymmaster3000.locker.domain.event.RentLockerEvent;
import gymmaster3000.locker.domain.event.SetupLockerEvent;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@EqualsAndHashCode(of = "lockerId")
public class Locker {

    private static final String LOG_SET_UP_LOCKER = "Set up a new locker with id=%s with event: %s";
    private static final String LOG_RENTED_LOCKER = "Rented a locker with id=%s with event: %s";
    private static final String LOG_RELEASED_LOCKER = "Released %s locker with id=%s with event: %s";
    private static final String AVAILABLE = "an available";
    private static final String RENTED = "a rented";
    @Getter
    private long version;
    private int sequenceNumer;
    private List<IncomingEvent> pendingEvents = new ArrayList<>();
    @Getter
    private LockerId lockerId;
    private RenterId currentRenterId;

    private Locker(long version, int sequenceNumer) {
        this.version = version;
        this.sequenceNumer = sequenceNumer;
    }

    public static Locker setup() {
        return new Locker();
    }

    private Locker() {
        var setup = SetupLockerEvent.builder()
                                    .createDate(LocalDateTime.now())
                                    .sequenceNumber(sequenceNumer++)
                                    .lockerId(LockerId.of(UUID.randomUUID()))
                                    .build();
        log.info(LOG_SET_UP_LOCKER.formatted(setup.getLockerId()
                                                  .value(), setup.toString()));
        appendAndHandle(setup);
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
        log.info(LOG_RENTED_LOCKER.formatted(this.lockerId.value(), rent.toString()));
        appendAndHandle(rent);
    }

    private void handle(RentLockerEvent rent) {
        this.currentRenterId = rent.getRenterId();
    }

    public void release() {
        var release = ReleaseLockerEvent.builder()
                                        .createDate(LocalDateTime.now())
                                        .sequenceNumber(sequenceNumer++)
                                        .build();
        var lockerStatus = this.currentRenterId == null ? AVAILABLE : RENTED;
        log.info(LOG_RELEASED_LOCKER.formatted(lockerStatus,
                                               this.lockerId.value(),
                                               release.toString()));
        appendAndHandle(release);
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

    public static Locker recreate(long version, List<IncomingEvent> events) {
        var locker = new Locker(version, events.size());
        events.forEach(locker::handleDispatcher);
        return locker;
    }

    private void appendAndHandle(IncomingEvent event) {
        pendingEvents.add(event);
        handleDispatcher(event);
    }

    private void handleDispatcher(IncomingEvent event) {
        if (event instanceof SetupLockerEvent setup) {
            handle(setup);
        } else if (event instanceof RentLockerEvent rent) {
            handle(rent);
        } else if (event instanceof ReleaseLockerEvent release) {
            handle(release);
        } else {
            throw new LockerDispatchedNotImplementedEventException(this.lockerId.value(), event.toString());
        }
    }

    public List<IncomingEvent> getAndClearPendingEvents() {
        var events = pendingEvents;
        pendingEvents = new ArrayList<>();
        return events;
    }

}
