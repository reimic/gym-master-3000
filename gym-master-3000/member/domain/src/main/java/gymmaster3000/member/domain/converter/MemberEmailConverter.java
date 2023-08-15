package gymmaster3000.member.domain.converter;

import gymmaster3000.member.domain.valueobject.MemberEmail;
import jakarta.persistence.AttributeConverter;

public class MemberEmailConverter implements AttributeConverter<MemberEmail, String> {

    @Override
    public String convertToDatabaseColumn(final MemberEmail attribute) {
        return attribute.value();
    }

    @Override
    public MemberEmail convertToEntityAttribute(final String dbData) {
        return MemberEmail.of(dbData);
    }

}
