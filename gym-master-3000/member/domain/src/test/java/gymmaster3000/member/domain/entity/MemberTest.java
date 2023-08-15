package gymmaster3000.member.domain.entity;

import gymmaster3000.member.domain.valueobject.MemberEmail;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    void shouldSignUpNewMember() {
        // given-when
        var member = Member.signUp();
        // then
        assertThat(member)
                .isNotNull()
                .extracting(Member::getMemberId)
                .isNotNull();
        assertThat(member)
                .extracting(Member::getMemberEmail)
                .isNull();
    }

    @Test
    void shouldSetEmail() {
        // given
        var member = Member.signUp();
        assertThat(member)
                .extracting(Member::getMemberEmail)
                .isNull();
        var email = "test@test";
        var memberEmail = MemberEmail.of(email);
        // when
        var memberWithEmailSet = member.withNewEmail(memberEmail);
        // then
        assertThat(memberWithEmailSet)
                .extracting(Member::getMemberEmail)
                .isNotNull()
                .extracting(MemberEmail::value)
                .isEqualTo(email);
        assertThat(memberWithEmailSet).isNotSameAs(member);
    }

    @Test
    void shouldReturnSelf_whenAskedToSetSameEmail() {
        // given
        var email = "test@test";
        var memberEmail = MemberEmail.of(email);
        var member = Member.signUp()
                           .withNewEmail(memberEmail);
        assertThat(member)
                .extracting(Member::getMemberEmail)
                .isNotNull()
                .extracting(MemberEmail::value)
                .isEqualTo(email);
        // when
        var memberWithNewEmail = member.withNewEmail(memberEmail);
        // then
        assertThat(memberWithNewEmail).isSameAs(member);
    }

}