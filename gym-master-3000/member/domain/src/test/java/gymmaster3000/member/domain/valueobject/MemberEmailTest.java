package gymmaster3000.member.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberEmailTest {

    @Test
    void shouldThrowException_whenValueProvidedIsNull() {
        // given-when-then
        assertThatThrownBy(() -> MemberEmail.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberEmail.INVALID_EMAIL.formatted("null"));
    }

    @Test
    void shouldThrowException_whenValueProvidedIsNotAValidEmail() {
        // given
        var value = "test";
        // when-then
        assertThatThrownBy(() -> MemberEmail.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberEmail.INVALID_EMAIL.formatted(value));
    }

    @Test
    void shouldReturnMemberEmailValueObject() {
        // given
        var value = "test@test";
        // when
        var memberEmail = MemberEmail.of(value);
        // then
        assertThat(memberEmail)
                .isNotNull()
                .isInstanceOf(MemberEmail.class)
                .extracting(MemberEmail::value)
                .isEqualTo(value);
    }

}