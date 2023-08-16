package gymmaster3000.locker.adapter.in.web;

import gymmaster3000.locker.application.usecase.FindAllLockersByUseCase;
import gymmaster3000.locker.domain.entity.LockerView;
import gymmaster3000.locker.domain.valueobject.RenterId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static gymmaster3000.locker.application.usecase.FindAllLockersByUseCase.FindAllLockersByQuery;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FindAllLockersByRestAdapter {

    private final FindAllLockersByUseCase useCase;

    @GetMapping("lockers/findAll")
    public ResponseEntity<String> findAllLockersBy(@RequestParam(name = "renterId", required = false) UUID renterId) {
        var query = new FindAllLockersByQuery(renterId != null ? RenterId.of(renterId) : null);
        var lockerViews = useCase.apply(query);
        var lockerJSONs = lockerViews.stream()
                                     .map(LockerView::toJSON)
                                     .toList()
                                     .toString();
        return new ResponseEntity<>(lockerJSONs, HttpStatus.OK);
    }

}
