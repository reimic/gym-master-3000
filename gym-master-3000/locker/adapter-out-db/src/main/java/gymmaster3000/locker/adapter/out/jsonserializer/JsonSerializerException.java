package gymmaster3000.locker.adapter.out.jsonserializer;

import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import org.springframework.context.annotation.Profile;

@Profile(SerializerProfile.JSON_SERIALIZER)
public class JsonSerializerException extends RuntimeException {

    public static final String READ_NOT_IMPLEMENTED_EVENT = "JSON Serializer attempted to read a not implemented eventType=%s.";
    public static final String READ_MALFORMED_EVENT = "JSON Serializer attempted to read a malformed event=%s.";

    private JsonSerializerException(String message) {
        super(message);
    }

    public static JsonSerializerException notImplementedEvent(String eventType) {
        return new JsonSerializerException(READ_NOT_IMPLEMENTED_EVENT.formatted(eventType));
    }

    public static JsonSerializerException malformedEvent(String malformedEvent) {
        return new JsonSerializerException(READ_MALFORMED_EVENT.formatted(malformedEvent));
    }

}
