package gymmaster3000.member.domain.converter;

import gymmaster3000.member.domain.valueobject.MemberEmail;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberEmailConverterTest {

    private final MemberEmailConverter testedObject = new MemberEmailConverter();

    @Test
    void shouldConvertFromDbDataToMemberEmail(){
        // given
        var dbData = "test@test";
        // when
        var memberEmail = testedObject.convertToEntityAttribute(dbData);
        // then
        assertThat(memberEmail)
                .isNotNull()
                .extracting(MemberEmail::value)
                .isEqualTo(dbData);
    }

    @Test
    void shouldConvertFromMemberEmailToDbData(){
        // given
        var email = "test@test";
        var memberEmail = MemberEmail.of(email);
        // when
        var dbData = testedObject.convertToDatabaseColumn(memberEmail);
        // then
        assertThat(dbData)
                .isNotNull()
                .isEqualTo(email);
    }

}