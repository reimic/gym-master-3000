package gymmaster3000.member.application.usecase;

import gymmaster3000.member.application.port.FindPreferredLockersPort;
import gymmaster3000.member.domain.valueobject.MemberId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class FindPreferredLockersUseCase {

    private final FindPreferredLockersPort port;

    public List<UUID> findPreferredLockers(@Valid @NonNull FindPreferredLockersQuery query) {
        return port.findBy(query.memberId)
                   .entrySet()
                   .stream()
                   .filter(entry -> entry.getValue()
                                         .intValue() > 0)
                   .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                   .limit(query.quantity != null ? query.quantity : Long.MAX_VALUE)
                   .map(Map.Entry::getKey)
                   .toList();
    }

    public record FindPreferredLockersQuery(
            @NotNull MemberId memberId,
            @Nullable Long quantity
    ) {

    }

}
