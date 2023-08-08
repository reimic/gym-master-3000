package gymmaster3000.member.domain.converter;

import gymmaster3000.member.domain.valueobject.MemberEmail;
import jakarta.persistence.AttributeConverter;

public class MemberEmailConverter implements AttributeConverter<MemberEmail, String> {
    @Override
    public String convertToDatabaseColumn(MemberEmail attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.value();
    }

    @Override
    public MemberEmail convertToEntityAttribute(String dbData) {
        return new MemberEmail(dbData);
    }

}
