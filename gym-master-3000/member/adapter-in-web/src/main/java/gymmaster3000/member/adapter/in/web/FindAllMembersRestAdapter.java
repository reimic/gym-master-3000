package gymmaster3000.member.adapter.in.web;

import gymmaster3000.member.application.usecase.FindAllMembersUseCase;
import gymmaster3000.member.domain.entity.MemberView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static gymmaster3000.member.application.usecase.FindAllMembersUseCase.GetAllMembersQuery;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FindAllMembersRestAdapter {

    private final FindAllMembersUseCase useCase;

    @GetMapping("members/findAll")
    public ResponseEntity<String> findAllMembers() {
        var query = new GetAllMembersQuery();
        var memberViews = useCase.apply(query);
        var memberJSONs = memberViews.stream()
                                     .map(MemberView::toJSON)
                                     .toList()
                                     .toString();
        return new ResponseEntity<>(memberJSONs, HttpStatus.OK);

    }

}
