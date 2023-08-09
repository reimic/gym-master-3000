package gymmaster3000.member.application.usecase;

import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.locker.domain.valueobject.RenterId;
import gymmaster3000.member.application.port.FindAllLockersPort;
import gymmaster3000.member.application.port.SaveAllLockersPort;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class ReleaseAllLockersByRenterUseCase {

    private final FindAllLockersPort findAllLockersPort;
    private final SaveAllLockersPort saveAllLockersPort;

    @Transactional
    public void apply(@Valid @NonNull ReleaseAllLockersByRenterCommand command) {
        var lockers = findAllLockersPort.findAll()
                                        .stream()
                                        .filter(locker -> locker.isRentedBy(command.renterId))
                                        .toList();
        lockers.forEach(Locker::release);
        saveAllLockersPort.saveAll(lockers);
    }

    public record ReleaseAllLockersByRenterCommand(@NonNull RenterId renterId) {

    }

}
