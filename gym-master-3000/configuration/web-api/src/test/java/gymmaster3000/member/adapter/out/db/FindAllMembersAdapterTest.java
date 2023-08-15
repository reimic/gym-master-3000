package gymmaster3000.member.adapter.out.db;

import gymmaster3000.configuration.GymMaster3000WebApiApplication;
import gymmaster3000.member.domain.entity.Member;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = GymMaster3000WebApiApplication.class)
class FindAllMembersAdapterTest {
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clearDb() {
        memberRepository.deleteAll();
    }

    @Test
    void shouldReturnListWith2Elements_when2Members_InDatabase(){
        // given
        var persistedMember1 = persistMember();
        var persistedMember2 = persistMember();
        // when
        var result = memberRepository.findAll();
        // then
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .anySatisfy(member -> assertThat(member.getMemberId()).isEqualTo(persistedMember1.getMemberId()))
                .anySatisfy(member -> assertThat(member.getMemberId()).isEqualTo(persistedMember2.getMemberId()));
    }

    @Test
    void shouldReturnEmptyList_whenNoMembers_InDatabase(){
        // given-when
        var result = memberRepository.findAll();
        // then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    private Member persistMember() {
        var email = "test@" + UUID.randomUUID().toString();
        var memberEmail = MemberEmail.of(email);
        var member = Member.signUp()
                           .withNewEmail(memberEmail);
        return memberRepository.save(member);
    }
}
