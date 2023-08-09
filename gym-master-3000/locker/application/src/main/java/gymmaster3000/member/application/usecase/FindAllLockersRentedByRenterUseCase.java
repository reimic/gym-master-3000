package gymmaster3000.member.application.usecase;

import gymmaster3000.locker.domain.entity.LockerView;
import gymmaster3000.locker.domain.valueobject.RenterId;
import gymmaster3000.member.application.port.FindAllLockersPort;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class FindAllLockersRentedByRenterUseCase {

    private final FindAllLockersPort port;

    public List<LockerView> apply(@Valid @NonNull FindAllLockersRentedByRenterQuery query) {
        return port.findAll()
                   .stream()
                   .filter(locker -> locker.isRentedBy(query.renterId))
                   .map(LockerView::from)
                   .toList();
    }

    public record FindAllLockersRentedByRenterQuery(@NonNull RenterId renterId) {

    }

}
