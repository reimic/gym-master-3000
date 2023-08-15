package gymmaster3000.member.application.usecase;

import gymmaster3000.member.application.port.SaveMemberPort;
import gymmaster3000.member.domain.entity.Member;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import gymmaster3000.member.domain.valueobject.MemberId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static gymmaster3000.member.application.usecase.SignUpMemberUseCase.SignUpMemberCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignUpMemberUseCaseTest {

    @InjectMocks
    private SignUpMemberUseCase testedObject;

    @Mock
    private SaveMemberPort saveMemberPort;

    @Test
    void shouldSignUpANewMember() {
        // given
        var email = "test@test";
        var memberEmail = MemberEmail.of(email);
        doAnswer(invocation -> {
            var argument = invocation.getArgument(0, Member.class);
            assertThat(argument.getMemberId())
                    .isNotNull()
                    .isInstanceOf(MemberId.class);
            assertThat(argument.getMemberEmail())
                    .isNotNull()
                    .isInstanceOf(MemberEmail.class)
                    .isEqualTo(memberEmail);
            return argument.getMemberId()
                           .value();
        })
                .when(saveMemberPort)
                .save(any(Member.class));
        var command = new SignUpMemberCommand(memberEmail);
        // when
        var result = testedObject.apply(command);
        // then
        assertThat(result).isNotNull()
                          .isInstanceOf(UUID.class);
        verify(saveMemberPort).save(any(Member.class));
    }

    @Test
    void shouldThrowException_whenInvalidCommandProvided() {
        // given
        var command = new SignUpMemberCommand(null);
        // when-then
        assertThatThrownBy(() -> testedObject.apply(command))
                .isInstanceOf(NullPointerException.class);
    }

}