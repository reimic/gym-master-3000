package gymmaster3000.locker.adapter.in.web;

import gymmaster3000.locker.application.usecase.ReleaseLockerUseCase;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.valueobject.LockerId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static gymmaster3000.locker.application.usecase.ReleaseLockerUseCase.ReleaseLockerCommand;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ReleaseLockerRestAdapter {

    private final ReleaseLockerUseCase useCase;

    @PostMapping("lockers/release")
    public ResponseEntity<String> releaseLocker(@RequestParam(name = "lockerId") UUID lockerId) {
        var command = new ReleaseLockerCommand(LockerId.of(lockerId));
        try {
            useCase.apply(command);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (LockerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
