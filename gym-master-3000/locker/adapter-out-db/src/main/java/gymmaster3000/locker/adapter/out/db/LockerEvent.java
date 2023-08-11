package gymmaster3000.locker.adapter.out.db;

import java.io.Serializable;

public interface LockerEvent<S extends Serializable> {

    long getEventId();

    S getContent();

    void setEventId(long eventId);

    void setContent(S content);

}
