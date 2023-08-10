package gymmaster3000.locker.application.usecase;

import gymmaster3000.locker.application.port.FindAllLockersPort;
import gymmaster3000.locker.application.port.SaveAllLockersPort;
import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.locker.domain.valueobject.RenterId;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class ReleaseAllLockersByUseCase {

    private final FindAllLockersPort findAllLockersPort;
    private final SaveAllLockersPort saveAllLockersPort;

    @Transactional
    public void apply(@Valid @NonNull ReleaseAllLockersByCommand command) {
        var lockers = findAllLockersPort.findAll()
                                        .stream()
                                        .filter(locker -> locker.isRentedBy(command.renterId))
                                        .toList();
        lockers.forEach(Locker::release);
        saveAllLockersPort.saveAll(lockers);
    }

    public record ReleaseAllLockersByCommand(@NonNull RenterId renterId) {

    }

}
