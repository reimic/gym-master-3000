package gymmaster3000.locker.adapter.in.web;

import gymmaster3000.locker.application.usecase.RentLockerUseCase;
import gymmaster3000.locker.domain.entity.LockerCurrentlyRentedException;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static gymmaster3000.locker.application.usecase.RentLockerUseCase.RentLockerCommand;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Validated
public class RentLockerRestAdapter {

    private final RentLockerUseCase useCase;

    @PostMapping("lockers/rent")
    public ResponseEntity<String> rentLocker(@Valid @RequestBody RentLockerRequest request) {
        var command = new RentLockerCommand(LockerId.of(request.lockerId), RenterId.of(request.renterId));
        try {
            useCase.apply(command);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (LockerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LockerCurrentlyRentedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    public record RentLockerRequest(
            @NotNull UUID lockerId,
            @NotNull UUID renterId) {

    }

}
