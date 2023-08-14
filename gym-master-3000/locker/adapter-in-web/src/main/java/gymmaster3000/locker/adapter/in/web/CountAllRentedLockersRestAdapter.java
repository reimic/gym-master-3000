package gymmaster3000.locker.adapter.in.web;

import gymmaster3000.locker.application.usecase.CountAllRentedLockersUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static gymmaster3000.locker.application.usecase.CountAllRentedLockersUseCase.CountAllRentedLockersQuery;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CountAllRentedLockersRestAdapter {

    private final CountAllRentedLockersUseCase useCase;

    @GetMapping("lockers/findAll/count/rented")
    public ResponseEntity<String> countAllRentedLockers() {
        var query = new CountAllRentedLockersQuery();
        var count = useCase.apply(query);
        return new ResponseEntity<>(Long.toString(count), HttpStatus.OK);
    }

}
