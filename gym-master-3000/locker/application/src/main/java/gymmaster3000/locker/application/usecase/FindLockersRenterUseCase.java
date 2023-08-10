package gymmaster3000.locker.application.usecase;

import gymmaster3000.locker.application.port.FindLockerPort;
import gymmaster3000.locker.domain.entity.LockerCurrentlyNotRentedException;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class FindLockersRenterUseCase {

    private final FindLockerPort port;

    @Transactional
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
