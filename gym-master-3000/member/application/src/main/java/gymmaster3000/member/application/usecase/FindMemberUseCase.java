package gymmaster3000.member.application.usecase;

import gymmaster3000.member.application.port.FindMemberPort;
import gymmaster3000.member.domain.entity.MemberNotFoundException;
import gymmaster3000.member.domain.entity.MemberView;
import gymmaster3000.member.domain.valueobject.MemberId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class FindMemberUseCase {

    private final FindMemberPort findMemberPort;

    public MemberView apply(@Valid GetMemberQuery query) {
        var member = findMemberPort.findBy(query.memberId)
                                   .orElseThrow(() -> new MemberNotFoundException(query.memberId.value()));
        return MemberView.from(member);
    }

    public record GetMemberQuery(
            @NotNull MemberId memberId
    ) {

    }

}
