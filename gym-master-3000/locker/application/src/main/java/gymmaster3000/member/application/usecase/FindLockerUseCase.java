package gymmaster3000.member.application.usecase;

import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.entity.LockerView;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.member.application.port.FindLockerPort;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class FindLockerUseCase {

    private final FindLockerPort port;

    public LockerView apply(@Valid @NonNull FindLockerQuery query) {
        var locker = port.findBy(query.lockerId)
                         .orElseThrow(() -> new LockerNotFoundException(query.lockerId.value()));
        return LockerView.from(locker);
    }

    public record FindLockerQuery(
            @NonNull LockerId lockerId
    ) {

    }

}
