package gymmaster3000.locker.domain.event;

import gymmaster3000.locker.domain.valueobject.RenterId;
import lombok.*;

import java.time.LocalDateTime;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RentLockerEvent implements IncomingEvent {

    LocalDateTime createDate;
    int sequenceNumber;
    RenterId renterId;

}
