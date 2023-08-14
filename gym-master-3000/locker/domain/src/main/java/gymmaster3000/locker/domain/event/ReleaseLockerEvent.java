package gymmaster3000.locker.domain.event;

import lombok.*;

import java.time.LocalDateTime;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReleaseLockerEvent implements IncomingEvent {

    LockerEventType eventType = LockerEventType.RELEASE_LOCKER;
    LocalDateTime createDate;
    int sequenceNumber;

}
