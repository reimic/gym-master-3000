package gymmaster3000.member.application.usecase;

import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.member.application.port.FindLockerPort;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class CheckIfLockerRentedUseCase {

    private final FindLockerPort port;

    public boolean apply(@Valid @NonNull CheckIfLockerRentedQuery query) {
        return port.findBy(query.lockerId)
                   .map(Locker::isRented)
                   .orElseThrow(() -> new LockerNotFoundException(query.lockerId.value()));
    }

    public record CheckIfLockerRentedQuery(@NonNull LockerId lockerId) {

    }

}
