package gymmaster3000.member.application.usecase;

import gymmaster3000.member.application.port.FindMemberPort;
import gymmaster3000.member.domain.entity.Member;
import gymmaster3000.member.domain.entity.MemberNotFoundException;
import gymmaster3000.member.domain.entity.MemberView;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import gymmaster3000.member.domain.valueobject.MemberId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static gymmaster3000.member.application.usecase.FindMemberUseCase.FindMemberQuery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindMemberUseCaseTest {

    @InjectMocks
    private FindMemberUseCase testedObject;

    @Mock
    private FindMemberPort findMemberPort;

    @Test
    void shouldFindMember_whenMemberIdValid() {
        // given
        var email = "test@test";
        var memberEmail = MemberEmail.of(email);
        var member = Member.signUp()
                           .withNewEmail(memberEmail);
        var memberId = member.getMemberId();
        var query = new FindMemberQuery(memberId);
        when(findMemberPort.findBy(memberId)).thenReturn(Optional.of(member));
        // when
        var result = testedObject.apply(query);
        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(MemberView.class)
                .extracting(MemberView::memberId)
                .isEqualTo(memberId.value());
        assertThat(result)
                .extracting(MemberView::memberEmail)
                .isEqualTo(memberEmail.value());
        verify(findMemberPort).findBy(any(MemberId.class));
    }

    @Test
    void shouldThrowException_whenMemberNotFound() {
        //given
        var invalidMemberId = MemberId.of(UUID.randomUUID());
        var query = new FindMemberQuery(invalidMemberId);
        when(findMemberPort.findBy(invalidMemberId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> testedObject.apply(query))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage(MemberNotFoundException.MEMBER_NOT_FOUND, invalidMemberId.value());
    }

}