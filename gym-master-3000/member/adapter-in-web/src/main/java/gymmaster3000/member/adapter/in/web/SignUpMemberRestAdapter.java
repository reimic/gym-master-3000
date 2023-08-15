package gymmaster3000.member.adapter.in.web;

import gymmaster3000.member.application.usecase.SignUpMemberUseCase;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static gymmaster3000.member.application.usecase.SignUpMemberUseCase.SignUpMemberCommand;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class SignUpMemberRestAdapter {

    public static final String EMAIL_ALREADY_ASSIGNED = "Provided email=%s is already assigned to an existing account.";
    private final SignUpMemberUseCase useCase;

    @PostMapping("members/signup{memberEmail}")
    public ResponseEntity<String> signUpMember(@RequestParam(name = "memberEmail") String email) {
        try {
            var memberEmail = MemberEmail.of(email);
            var command = new SignUpMemberCommand(memberEmail);
            var memberId = useCase.apply(command);
            return new ResponseEntity<>(String.valueOf(memberId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(String.valueOf(EMAIL_ALREADY_ASSIGNED.formatted(email)),
                                        HttpStatus.OK);
        }
    }

}
