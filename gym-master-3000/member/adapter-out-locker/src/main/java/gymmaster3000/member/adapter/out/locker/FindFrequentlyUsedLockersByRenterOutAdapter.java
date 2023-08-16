package gymmaster3000.member.adapter.out.locker;

import gymmaster3000.locker.adapter.in.member.FindFrequentlyUsedLockersByRenterInAdapter;
import gymmaster3000.locker.domain.valueobject.RenterId;
import gymmaster3000.member.application.port.FindPreferredLockersPort;
import gymmaster3000.member.domain.valueobject.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Validated
public class FindFrequentlyUsedLockersByRenterOutAdapter implements FindPreferredLockersPort {

    private final FindFrequentlyUsedLockersByRenterInAdapter moduleLocker;

    @Override
    public Map<UUID, Long> findBy(final MemberId memberId) {
        return moduleLocker.findFrequentlyUsedLockers(RenterId.of(memberId.value()))
                           .entrySet()
                           .stream()
                           .collect(Collectors.toMap(
                                   entry -> entry.getKey()
                                                 .value(),
                                   Map.Entry::getValue,
                                   (first, conflict) -> first,
                                   LinkedHashMap::new));
    }

}
