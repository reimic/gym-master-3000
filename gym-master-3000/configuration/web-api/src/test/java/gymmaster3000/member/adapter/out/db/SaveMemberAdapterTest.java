package gymmaster3000.member.adapter.out.db;

import gymmaster3000.configuration.GymMaster3000WebApiApplication;
import gymmaster3000.member.domain.entity.Member;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = GymMaster3000WebApiApplication.class)
class SaveMemberAdapterTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clearDb(){
        memberRepository.deleteAll();
    }

    @Test
    void shouldSaveMember() {
        // given
        var email = "test@test";
        var memberEmail = MemberEmail.of(email);
        var member = Member.signUp()
                           .withNewEmail(memberEmail);
        var memberId = member.getMemberId();
        // when
        var result = memberRepository.save(member);
        // then
        assertThat(result)
                .isNotNull()
                .extracting(Member::getMemberId)
                .isEqualTo(memberId);
        var found = memberRepository.findById(memberId);
        assertThat(found)
                .isPresent()
                .hasValueSatisfying(foundMember -> assertThat(foundMember.getMemberId()).isEqualTo(memberId));
    }

    @Test
    void shouldThrowException_whenSavingMember_withEmailSameAsAlreadyInDatabase() {
        // given
        var email = "test@test";
        var memberEmail = MemberEmail.of(email);
        var member1 = Member.signUp()
                            .withNewEmail(memberEmail);
        memberRepository.save(member1);
        var member2 = Member.signUp()
                            .withNewEmail(memberEmail);
        // when-then
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

}