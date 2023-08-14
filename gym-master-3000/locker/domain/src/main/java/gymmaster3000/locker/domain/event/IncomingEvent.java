package gymmaster3000.locker.domain.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface IncomingEvent extends Serializable {

    LockerEventType getEventType();

    LocalDateTime getCreateDate();

    int getSequenceNumber();

}
