package gymmaster3000.locker.application.usecase;

import gymmaster3000.locker.application.port.FindLockerPort;
import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.valueobject.LockerId;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class CheckIfLockerRentedUseCase {

    private final FindLockerPort port;

    @Transactional
    public boolean apply(@Valid @NonNull CheckIfLockerRentedQuery query) {
        return port.findBy(query.lockerId)
                   .map(Locker::isRented)
                   .orElseThrow(() -> new LockerNotFoundException(query.lockerId.value()));
    }

    public record CheckIfLockerRentedQuery(@NonNull LockerId lockerId) {

    }

}
