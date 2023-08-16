package gymmaster3000.member.adapter.in.web;

import gymmaster3000.member.application.usecase.FindPreferredLockersUseCase;
import gymmaster3000.member.domain.valueobject.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static gymmaster3000.member.application.usecase.FindPreferredLockersUseCase.FindPreferredLockersQuery;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FindPreferredLockersRestAdapter {

    private final FindPreferredLockersUseCase useCase;

    @GetMapping("members/preferred/lockers")
    public ResponseEntity<String> signUpMember(@RequestParam(name = "memberId") UUID id,
                                               @RequestParam(name = "quantity", required = false) Long quantity) {
        var memberId = MemberId.of(id);
        var query = new FindPreferredLockersQuery(memberId, quantity);
        var frequency = useCase.findPreferredLockers(query);
        return new ResponseEntity<>(frequency.toString(), HttpStatus.OK);
    }

}
