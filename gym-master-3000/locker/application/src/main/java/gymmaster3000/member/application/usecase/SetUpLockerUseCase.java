package gymmaster3000.member.application.usecase;

import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.member.application.port.SaveLockerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SetUpLockerUseCase {

    private final SaveLockerPort port;

    @Transactional
    public UUID apply(SetUpLockerCommand command) {
        var locker = Locker.create();
        port.save(locker);
        return locker.getLockerId()
                     .value();
    }

    public record SetUpLockerCommand() {

    }

}
