package gymmaster3000.locker.adapter.out.db;

import java.io.Serializable;
import java.util.Set;

public interface LockerEventStream<S extends Serializable, E extends LockerEvent<S>> {

    long getVersion();

    Set<E> getEvents();

}
