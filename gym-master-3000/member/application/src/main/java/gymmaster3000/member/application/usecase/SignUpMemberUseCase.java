package gymmaster3000.member.application.usecase;

import gymmaster3000.member.application.port.SaveMemberPort;
import gymmaster3000.member.domain.entity.Member;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class SignUpMemberUseCase {

    private final SaveMemberPort port;

    @Transactional
    public UUID apply(@Valid SignUpMemberCommand command) {
        var member = Member.create()
                           .withNewEmail(command.memberEmail);
        return port.save(member);
    }

    public record SignUpMemberCommand(@NotNull MemberEmail memberEmail) {

    }

}
