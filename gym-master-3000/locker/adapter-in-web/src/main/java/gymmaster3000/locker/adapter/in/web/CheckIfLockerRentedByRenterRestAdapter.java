package gymmaster3000.locker.adapter.in.web;

import gymmaster3000.locker.application.usecase.CheckIfLockerRentedByRenterUseCase;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static gymmaster3000.locker.application.usecase.CheckIfLockerRentedByRenterUseCase.CheckIfLockerRentedByRenterQuery;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CheckIfLockerRentedByRenterRestAdapter {

    private final CheckIfLockerRentedByRenterUseCase useCase;

    @GetMapping("lockers/find/isrentedby")
    public ResponseEntity<String> isRentedBy(@RequestParam(name = "lockerId") UUID lockerId,
                                             @RequestParam(name = "renterId") UUID renterId) {
        var query = new CheckIfLockerRentedByRenterQuery(LockerId.of(lockerId), RenterId.of(renterId));
        try {
            var isRentedBy = useCase.apply(query);
            return new ResponseEntity<>(Boolean.toString(isRentedBy), HttpStatus.OK);
        } catch (LockerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
