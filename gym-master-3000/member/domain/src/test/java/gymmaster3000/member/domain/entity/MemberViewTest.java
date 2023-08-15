package gymmaster3000.member.domain.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberViewTest {

    @Test
    void shouldCreateViewFromMember() {
        // given
        var memberEmail = MemberEmail.of("test@test");
        var member = Member.signUp()
                           .withNewEmail(memberEmail);
        // when
        var memberView = MemberView.from(member);
        // then
        assertThat(memberView)
                .isNotNull()
                .isInstanceOf(MemberView.class);
        assertThat(memberView)
                .extracting(MemberView::memberId)
                .isNotNull()
                .isEqualTo(member.getMemberId()
                                 .value());
        assertThat(memberView)
                .extracting(MemberView::memberEmail)
                .isNotNull()
                .isEqualTo(member.getMemberEmail()
                                 .value());
    }

    @Test
    void shouldReturnValidJsonAsString() {
        // given
        var memberEmail = MemberEmail.of("test@test");
        var member = Member.signUp()
                           .withNewEmail(memberEmail);
        var memberView = MemberView.from(member);
        // when
        var memberJSON = memberView.toJSON();
        // then
        assertThat(memberJSON).isNotBlank();
        assertThat(isValidJson(memberJSON)).isTrue();
    }

    private boolean isValidJson(String json) {
        var mapper = new ObjectMapper()
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

        try {
            mapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

}