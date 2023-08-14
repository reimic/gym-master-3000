package gymmaster3000.locker.adapter.in.web;

import gymmaster3000.locker.application.usecase.FindLockerUseCase;
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

import static gymmaster3000.locker.application.usecase.FindLockerUseCase.FindLockerQuery;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FindLockerRestAdapter {

    private final FindLockerUseCase useCase;

    @GetMapping("lockers/find")
    public ResponseEntity<String> findLocker(@RequestParam(name = "lockerId") UUID lockerId) {
        var query = new FindLockerQuery(LockerId.of(lockerId));
        try {
            var locker = useCase.apply(query);
            return new ResponseEntity<>(locker.toJSON(), HttpStatus.OK);
        } catch (LockerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
