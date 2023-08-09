package gymmaster3000.member.application.usecase;

import gymmaster3000.locker.domain.entity.LockerCurrentlyNotRentedException;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;
import gymmaster3000.member.application.port.FindLockerPort;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class FindLockersRenterUseCase {

    private final FindLockerPort port;

    public RenterId apply(@Valid @NonNull FindLockersRenterQuery query) {
        return port.findBy(query.lockerId)
                   .orElseThrow(() -> new LockerNotFoundException(query.lockerId.value()))
                   .getCurrentRenterId()
                   .orElseThrow(() -> new LockerCurrentlyNotRentedException(query.lockerId.value()));
    }

    public record FindLockersRenterQuery(
            @NonNull LockerId lockerId
    ) {

    }

}