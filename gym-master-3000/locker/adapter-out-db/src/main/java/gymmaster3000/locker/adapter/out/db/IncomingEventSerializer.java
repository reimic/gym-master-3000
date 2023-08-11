package gymmaster3000.locker.adapter.out.db;

import gymmaster3000.locker.domain.event.IncomingEvent;

import java.io.Serializable;
import java.util.UUID;

public interface IncomingEventSerializer<S extends Serializable, E extends LockerEvent<S>> {

    E serialize(IncomingEvent incomingEvent);

    IncomingEvent deserialize(E lockerEvent);

    LockerEventStream<S, E> createLockerEventStream(UUID lockerId);

}
