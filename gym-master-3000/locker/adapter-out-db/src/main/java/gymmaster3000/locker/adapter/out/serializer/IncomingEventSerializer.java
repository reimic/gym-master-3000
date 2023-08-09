package gymmaster3000.locker.adapter.out.serializer;

import gymmaster3000.locker.domain.event.IncomingEvent;

public interface IncomingEventSerializer {

    byte[] serialize(IncomingEvent incomingEvent);

    IncomingEvent deserialize(byte[] bytes);

}
