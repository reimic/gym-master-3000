package gymmaster3000.member.adapter.in.web;

import gymmaster3000.member.application.usecase.FindMemberUseCase;
import gymmaster3000.member.domain.entity.MemberNotFoundException;
import gymmaster3000.member.domain.valueobject.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static gymmaster3000.member.application.usecase.FindMemberUseCase.FindMemberQuery;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FindMemberRestAdapter {

    private final FindMemberUseCase useCase;

    @GetMapping("members/find")
    public ResponseEntity<String> findMember(@RequestParam(name = "memberId") UUID id) {
        try {
            var memberId = MemberId.of(id);
            var query = new FindMemberQuery(memberId);
            var memberView = useCase.apply(query);
            return new ResponseEntity<>(memberView.toJSON(), HttpStatus.OK);
        } catch (MemberNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
