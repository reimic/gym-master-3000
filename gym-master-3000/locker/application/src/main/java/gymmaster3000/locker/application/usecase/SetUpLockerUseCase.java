package gymmaster3000.locker.application.usecase;

import gymmaster3000.locker.application.port.SaveLockerPort;
import gymmaster3000.locker.domain.entity.Locker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetUpLockerUseCase {

    private static final String LOG_SET_UP_LOCKER = "Set up a new locker with id=%s";
    private final SaveLockerPort port;

    @Transactional
    public UUID apply(SetUpLockerCommand command) {
        var locker = Locker.setup();
        port.save(locker);
        var lockerId = locker
                .getLockerId()
                .value();
        log.info(LOG_SET_UP_LOCKER.formatted(lockerId));
        return lockerId;
    }

    public record SetUpLockerCommand() {

    }

}
