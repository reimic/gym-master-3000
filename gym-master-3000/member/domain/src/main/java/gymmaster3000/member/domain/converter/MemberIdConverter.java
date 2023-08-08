package gymmaster3000.member.domain.converter;

import gymmaster3000.member.domain.valueobject.MemberId;

import java.util.UUID;

public class MemberIdConverter extends UuidValueObjectConverter<MemberId> {

    @Override
    public MemberId convertToEntityAttribute(final UUID dbData) {
        return new MemberId(dbData);
    }

}
