package gymmaster3000.locker.adapter.in.web;

import gymmaster3000.locker.application.usecase.SetUpLockerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static gymmaster3000.locker.application.usecase.SetUpLockerUseCase.SetUpLockerCommand;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class SetUpLockerRestAdapter {

    private final SetUpLockerUseCase useCase;

    @PostMapping("lockers/setup")
    public ResponseEntity<String> setUpLocker() {
        var command = new SetUpLockerCommand();
        UUID lockerId = useCase.apply(command);
        return new ResponseEntity<>(String.valueOf(lockerId), HttpStatus.CREATED);
    }

}
