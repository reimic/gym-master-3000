package gymmaster3000.locker.application.usecase;

import gymmaster3000.locker.application.port.FindLockerPort;
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
public class CheckIfLockerRentedByRenterUseCase {

    private final FindLockerPort port;

    @Transactional
    public boolean apply(@Valid @NonNull CheckIfLockerRentedByRenterQuery query) {
        return port.findBy(query.lockerId)
                   .map(locker -> locker.isRentedBy(query.renterId))
                   .orElseThrow(() -> new LockerNotFoundException(query.lockerId.value()));
    }

    public record CheckIfLockerRentedByRenterQuery(
            @NonNull LockerId lockerId,
            @NonNull RenterId renterId) {

    }

}
