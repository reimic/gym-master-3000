package gymmaster3000.locker.application.usecase;

import gymmaster3000.locker.application.port.FindLockerPort;
import gymmaster3000.locker.application.port.SaveLockerPort;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.valueobject.LockerId;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class ReleaseLockerUseCase {

    private static final String LOG_RELEASED_LOCKER = "Released locker with id=%s";
    private static final String LOG_ATTEMPTED_TO_RELEASE_LOCKER = "Attempted to release not rented locker with id=%s";

    private final FindLockerPort findLockerPort;
    private final SaveLockerPort saveLockerPort;

    @Transactional
    public void apply(@Valid @NonNull ReleaseLockerCommand command) {
        var locker = findLockerPort.findBy(command.lockerId)
                                   .orElseThrow(() -> new LockerNotFoundException(command.lockerId.value()));
        if (locker.isRented()) {
            locker.release();
            saveLockerPort.save(locker);
            log.info(LOG_RELEASED_LOCKER.formatted(locker.getLockerId()
                                                         .value()));
        } else {
            log.info(LOG_ATTEMPTED_TO_RELEASE_LOCKER.formatted(locker.getLockerId()
                                                                     .value()));
        }

    }

    public record ReleaseLockerCommand(
            @NonNull LockerId lockerId
    ) {

    }

}
