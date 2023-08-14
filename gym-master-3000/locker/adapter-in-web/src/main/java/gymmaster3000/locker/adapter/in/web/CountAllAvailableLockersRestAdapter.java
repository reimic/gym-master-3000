package gymmaster3000.locker.adapter.in.web;

import gymmaster3000.locker.application.usecase.CountAllAvailableLockersUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static gymmaster3000.locker.application.usecase.CountAllAvailableLockersUseCase.CountAllAvailableLockersQuery;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CountAllAvailableLockersRestAdapter {

    private final CountAllAvailableLockersUseCase useCase;

    @GetMapping("lockers/findAll/count/available")
    public ResponseEntity<String> countAllAvailableLockers() {
        var query = new CountAllAvailableLockersQuery();
        var count = useCase.apply(query);
        return new ResponseEntity<>(Long.toString(count), HttpStatus.OK);
    }

}
