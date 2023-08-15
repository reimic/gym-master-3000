package gymmaster3000.member.application.usecase;

import gymmaster3000.member.application.port.FindAllMembersPort;
import gymmaster3000.member.domain.entity.Member;
import gymmaster3000.member.domain.entity.MemberView;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static gymmaster3000.member.application.usecase.FindAllMembersUseCase.GetAllMembersQuery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllMembersUseCaseTest {

    @InjectMocks
    private FindAllMembersUseCase testedObject;

    @Mock
    private FindAllMembersPort findAllMembersPort;

    @Test
    void shouldReturnListWith2Elements_whenDbContains2Members() {
        // given
        var members = getMembers();
        var query = new GetAllMembersQuery();
        when(findAllMembersPort.findAll()).thenReturn(members);
        var memberViews = members.stream()
                                 .map(MemberView::from)
                                 .toList();
        // when
        var result = testedObject.apply(query);
        // then
        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrderElementsOf(memberViews);
    }

    @Test
    void shouldReturnEmptyList_whenDbContainsNoMembers() {
        // given
        var query = new GetAllMembersQuery();
        when(findAllMembersPort.findAll()).thenReturn(List.of());
        // when
        var result = testedObject.apply(query);
        // then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    private static List<Member> getMembers() {
        var email1 = "test@test";
        var memberEmail1 = MemberEmail.of(email1);
        var member1 = Member.signUp()
                            .withNewEmail(memberEmail1);
        var email2 = "test@test";
        var memberEmail2 = MemberEmail.of(email2);
        var member2 = Member.signUp()
                            .withNewEmail(memberEmail2);
        return List.of(member1, member2);
    }

}