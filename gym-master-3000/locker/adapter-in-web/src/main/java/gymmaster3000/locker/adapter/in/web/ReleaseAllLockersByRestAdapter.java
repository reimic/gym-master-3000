package gymmaster3000.locker.adapter.in.web;

import gymmaster3000.locker.application.usecase.ReleaseAllLockersByUseCase;
import gymmaster3000.locker.domain.valueobject.RenterId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static gymmaster3000.locker.application.usecase.ReleaseAllLockersByUseCase.ReleaseAllLockersByCommand;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ReleaseAllLockersByRestAdapter {

    private final ReleaseAllLockersByUseCase useCase;

    @PostMapping("lockers/releaseAll")
    public ResponseEntity<String> releaseAllLockersByRenter(@RequestParam(name = "renterId") UUID renterId) {
        var command = new ReleaseAllLockersByCommand(RenterId.of(renterId));
        useCase.apply(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
