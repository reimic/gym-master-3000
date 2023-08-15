package gymmaster3000.member.adapter.out.db;

import gymmaster3000.configuration.GymMaster3000WebApiApplication;
import gymmaster3000.member.domain.entity.Member;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import gymmaster3000.member.domain.valueobject.MemberId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = GymMaster3000WebApiApplication.class)
class FindMemberAdapterTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clearDb() {
        memberRepository.deleteAll();
    }

    @Test
    void shouldReturnOptionalWithMember_whenMemberWithProvidedId_InDatabase() {
        // given
        var member = persistMember();
        var memberId = member.getMemberId();
        // when
        var result = memberRepository.findById(memberId);
        // then
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(value -> assertThat(value.getMemberId()).isEqualTo(memberId));
    }

    @Test
    void shouldReturnEmptyOptional_whenMemberWithProvidedId_notInDatabase() {
        // given
        var memberId = MemberId.of(UUID.randomUUID());
        // when
        var result = memberRepository.findById(memberId);
        // then
        assertThat(result)
                .isEmpty();
    }

    private Member persistMember() {
        var email = "test@test";
        var memberEmail = MemberEmail.of(email);
        var member = Member.signUp()
                           .withNewEmail(memberEmail);
        return memberRepository.save(member);
    }

}
