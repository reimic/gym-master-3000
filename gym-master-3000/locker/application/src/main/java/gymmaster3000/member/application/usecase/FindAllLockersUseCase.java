package gymmaster3000.member.application.usecase;

import gymmaster3000.locker.domain.entity.LockerView;
import gymmaster3000.member.application.port.FindAllLockersPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllLockersUseCase {

    private final FindAllLockersPort port;

    public List<LockerView> apply(FindAllLockersQuery query) {
        return port.findAll()
                   .stream()
                   .map(LockerView::from)
                   .toList();
    }

    public record FindAllLockersQuery() {

    }

}
