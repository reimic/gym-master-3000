package gymmaster3000.locker.adapter.in.web;

import gymmaster3000.locker.application.usecase.FindLockersRenterUseCase;
import gymmaster3000.locker.domain.entity.LockerCurrentlyNotRentedException;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.valueobject.LockerId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static gymmaster3000.locker.application.usecase.FindLockersRenterUseCase.FindLockersRenterQuery;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FindLockersRenterRestAdapter {

    private final FindLockersRenterUseCase useCase;

    @GetMapping("lockers/find/renter")
    public ResponseEntity<String> findLockersRenter(@RequestParam(name = "lockerId") UUID lockerId) {
        var query = new FindLockersRenterQuery(LockerId.of(lockerId));
        try {
            var renterId = useCase.apply(query);
            return new ResponseEntity<>(renterId.value()
                                                .toString(), HttpStatus.OK);
        } catch (LockerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LockerCurrentlyNotRentedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
