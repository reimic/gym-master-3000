package gymmaster3000.locker.application.usecase;

import gymmaster3000.locker.application.port.FindAllLockersPort;
import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class FindFrequentlyUsedLockersByRenterUseCase {

    private final FindAllLockersPort port;

    @Transactional
    @Cacheable("frequencies")
    public Map<LockerId, Long> apply(@Valid @NonNull FindFrequentlyUsedLockersByRenterUseCase.FindFrequentlyUsedLockersQuery query) {
        return port.findAll()
                   .stream()
                   .collect(Collectors.toMap(Locker::getLockerId,
                                             locker -> locker.getCountRentedBy(query.renterId)));

    }

    public record FindFrequentlyUsedLockersQuery(@NotNull RenterId renterId) {

    }

}
