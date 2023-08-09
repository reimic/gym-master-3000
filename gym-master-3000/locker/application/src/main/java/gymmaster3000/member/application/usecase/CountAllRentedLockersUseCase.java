package gymmaster3000.member.application.usecase;

import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.member.application.port.FindAllLockersPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountAllRentedLockersUseCase {

    private FindAllLockersPort port;

    public long apply(CountAllRentedLockersQuery query) {
        return port.findAll()
                   .stream()
                   .filter(Locker::isRented)
                   .count();
    }

    public record CountAllRentedLockersQuery() {

    }

}