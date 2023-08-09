package gymmaster3000.member.domain.converter;

import gymmaster3000.member.domain.valueobject.MemberId;
import jakarta.persistence.AttributeConverter;

import java.util.UUID;

public class MemberIdConverter implements AttributeConverter<MemberId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(final MemberId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.value();
    }

    @Override
    public MemberId convertToEntityAttribute(final UUID dbData) {
        return new MemberId(dbData);
    }

}
