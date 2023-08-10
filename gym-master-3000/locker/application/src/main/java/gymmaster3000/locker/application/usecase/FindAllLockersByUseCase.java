package gymmaster3000.locker.application.usecase;

import gymmaster3000.locker.application.port.FindAllLockersPort;
import gymmaster3000.locker.domain.entity.LockerView;
import gymmaster3000.locker.domain.valueobject.RenterId;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class FindAllLockersByUseCase {

    private final FindAllLockersPort port;

    public List<LockerView> apply(@Valid @NonNull FindAllLockersByQuery query) {
        return port.findAll()
                   .stream()
                   .filter(locker -> query.renterId == null || locker.isRentedBy(query.renterId))
                   .map(LockerView::from)
                   .toList();
    }

    public record FindAllLockersByQuery(@Nullable RenterId renterId) {

    }

}
