package gymmaster3000.locker.adapter.in.member;

import gymmaster3000.locker.application.usecase.FindFrequentlyUsedLockersByRenterUseCase;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

import static gymmaster3000.locker.application.usecase.FindFrequentlyUsedLockersByRenterUseCase.FindFrequentlyUsedLockersQuery;

@Component
@RequiredArgsConstructor
@Validated
public class FindFrequentlyUsedLockersByRenterInAdapter {

    private final FindFrequentlyUsedLockersByRenterUseCase useCase;

    public Map<LockerId, Long> findFrequentlyUsedLockers(@NotNull RenterId renterId) {
        var query = new FindFrequentlyUsedLockersQuery(renterId);
        return useCase.apply(query);
    }

}
