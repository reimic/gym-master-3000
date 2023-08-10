package gymmaster3000.locker.application.usecase;

import gymmaster3000.locker.application.port.FindLockerPort;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;
import gymmaster3000.locker.application.port.SaveLockerPort;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class RentLockerUseCase {

    private final FindLockerPort findLockerPort;
    private final SaveLockerPort saveLockerPort;

    @Transactional
    public void apply(@Valid @NonNull RentLockerCommand command) {
        var locker = findLockerPort.findBy(command.lockerId)
                                   .orElseThrow(() -> new LockerNotFoundException(command.lockerId.value()));
        locker.rent(command.renterId);
        saveLockerPort.save(locker);
    }

    public record RentLockerCommand(
            @NonNull LockerId lockerId,
            @NonNull RenterId renterId
    ) {

    }

}
