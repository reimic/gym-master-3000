package gymmaster3000.locker.adapter.out.db;

import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
public interface LockerEventStream<S extends Serializable, E extends LockerEvent<S>> {

    UUID getLockerId();

    long getVersion();

    Set<E> getEvents();

    void setLockerId(UUID lockerId);

    void setVersion(long version);

    void setEvents(Set<E> events);

}
