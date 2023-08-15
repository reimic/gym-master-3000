package gymmaster3000.member.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MemberIdTest {
    @Test
    void shouldThrowException_whenValueProvidedIsNull() {
        // given-when-then
        assertThatThrownBy(() -> MemberId.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberId.INVALID_ID.formatted("null"));
    }

    @Test
    void shouldReturnMemberIdValueObject() {
        // given
        var value = UUID.randomUUID();
        // when
        var memberId = MemberId.of(value);
        // then
        assertThat(memberId)
                .isNotNull()
                .isInstanceOf(MemberId.class)
                .extracting(MemberId::value)
                .isEqualTo(value);
    }
}