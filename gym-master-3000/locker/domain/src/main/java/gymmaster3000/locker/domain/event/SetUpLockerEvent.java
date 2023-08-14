package gymmaster3000.locker.domain.event;

import gymmaster3000.locker.domain.valueobject.LockerId;
import lombok.*;

import java.time.LocalDateTime;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SetUpLockerEvent implements IncomingEvent {

    LockerEventType eventType = LockerEventType.SET_UP_LOCKER;
    LocalDateTime createDate;
    int sequenceNumber;
    LockerId lockerId;

}
