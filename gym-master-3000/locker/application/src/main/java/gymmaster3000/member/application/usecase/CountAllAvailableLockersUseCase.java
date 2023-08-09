package gymmaster3000.member.application.usecase;

import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.member.application.port.FindAllLockersPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountAllAvailableLockersUseCase {

    private FindAllLockersPort port;

    public long apply(CountAllAvailableLockersQuery query) {
        return port.findAll()
                   .stream()
                   .filter(Locker::isAvailable)
                   .count();
    }

    public record CountAllAvailableLockersQuery() {

    }

}
