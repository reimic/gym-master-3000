package gymmaster3000.member.domain.converter;

import gymmaster3000.member.domain.valueobject.ValueObject;
import jakarta.persistence.AttributeConverter;

import java.util.UUID;

public abstract class UuidValueObjectConverter<T extends ValueObject<UUID>> implements AttributeConverter<T, UUID> {

    @Override
    public UUID convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.value();
    }

}
