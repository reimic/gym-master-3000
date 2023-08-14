package gymmaster3000.locker.adapter.out.jsonserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import gymmaster3000.locker.adapter.out.db.IncomingEventSerializer;
import gymmaster3000.locker.domain.event.IncomingEvent;
import gymmaster3000.locker.domain.event.ReleaseLockerEvent;
import gymmaster3000.locker.domain.event.RentLockerEvent;
import gymmaster3000.locker.domain.event.SetUpLockerEvent;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.locker.domain.valueobject.RenterId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static gymmaster3000.locker.domain.event.LockerEventType.*;

@Component
@Profile("json-serializer")
public class IncomingEventJsonSerializer implements IncomingEventSerializer<String, JsonSerializedLockerEvent> {

    private final ObjectMapper objectMapper;

    public IncomingEventJsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = configureObjectMapper(objectMapper);
    }

    private ObjectMapper configureObjectMapper(ObjectMapper objectMapper) {

        SimpleModule module = new SimpleModule("IncomingEventDeserializer", Version.unknownVersion());
        module.addDeserializer(IncomingEvent.class, IncomingEventDeserializer.getInstance());

        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                    .registerModule(module);
        return objectMapper;
    }

    private static class IncomingEventDeserializer extends StdDeserializer<IncomingEvent> {

        private static volatile IncomingEventDeserializer instance;
        public static final String FIELD_NAME_EVENT_TYPE = "eventType";
        public static final String FIELD_NAME_CREATE_DATE = "createDate";
        public static final String FIELD_NAME_SEQUENCE_NUMBER = "sequenceNumber";
        public static final String FIELD_NAME_LOCKER_ID = "lockerId";
        public static final String FIELD_NAME_RENTER_ID = "renterId";
        public static final String MALFORMED_SET_UP_EVENT = FIELD_NAME_EVENT_TYPE + ": %s, " + FIELD_NAME_CREATE_DATE + ": %s, " + FIELD_NAME_SEQUENCE_NUMBER + ": %s, " + FIELD_NAME_LOCKER_ID + ": %s";
        public static final String MALFORMED_RENT_EVENT = FIELD_NAME_EVENT_TYPE + ": %s, " + FIELD_NAME_CREATE_DATE + ": %s, " + FIELD_NAME_SEQUENCE_NUMBER + ": %s, " + FIELD_NAME_RENTER_ID + ": %s";
        public static final String MALFORMED_RELEASE_EVENT = FIELD_NAME_EVENT_TYPE + ": %s, " + FIELD_NAME_CREATE_DATE + ": %s, " + FIELD_NAME_SEQUENCE_NUMBER + ": %s";

        public static IncomingEventDeserializer getInstance() {
            IncomingEventDeserializer result = instance;
            if (result != null) {
                return result;
            }
            synchronized (IncomingEventDeserializer.class) {
                if (instance == null) {
                    instance = new IncomingEventDeserializer(IncomingEvent.class);
                }
                return instance;
            }
        }

        private IncomingEventDeserializer(Class<IncomingEvent> e) {
            super(e);
        }

        @Override
        public IncomingEvent deserialize(final JsonParser parser, final DeserializationContext context)
                throws IOException {

            String eventType = null;
            LocalDateTime createDate = null;
            Integer sequenceNumber = null;
            LockerId lockerId = null;
            RenterId renterId = null;

            while (!parser.isClosed()) {
                String fieldName = parser.nextFieldName();
                parser.nextValue();
                if (FIELD_NAME_EVENT_TYPE.equals(fieldName)) {
                    eventType = parser.getValueAsString();
                } else if (FIELD_NAME_CREATE_DATE.equals(fieldName)) {
                    createDate = parser.readValueAs(LocalDateTime.class);
                } else if (FIELD_NAME_SEQUENCE_NUMBER.equals(fieldName)) {
                    sequenceNumber = parser.getValueAsInt();
                } else if (FIELD_NAME_LOCKER_ID.equals(fieldName)) {
                    lockerId = parser.readValueAs(LockerId.class);
                } else if (FIELD_NAME_RENTER_ID.equals(fieldName)) {
                    renterId = parser.readValueAs(RenterId.class);
                }
            }

            if (SET_UP_LOCKER.eventName.equals(eventType)) {
                return getSetUpLockerEvent(eventType, createDate, sequenceNumber, lockerId);
            } else if (RENT_LOCKER.eventName.equals(eventType)) {
                return getRentLockerEvent(eventType, createDate, sequenceNumber, renterId);
            } else if (RELEASE_LOCKER.eventName.equals(eventType)) {
                return getReleaseLockerEvent(eventType, createDate, sequenceNumber);
            } else {
                throw JsonSerializerException.notImplementedEvent(eventType);
            }

        }

        private static ReleaseLockerEvent getReleaseLockerEvent(final String eventType,
                                                                final LocalDateTime createDate,
                                                                final Integer sequenceNumber) {
            if (createDate == null || sequenceNumber == null) {
                throw JsonSerializerException.malformedEvent(MALFORMED_RELEASE_EVENT.formatted(eventType,
                                                                                               createDate,
                                                                                               sequenceNumber));
            }
            return ReleaseLockerEvent.builder()
                                     .createDate(createDate)
                                     .sequenceNumber(sequenceNumber)
                                     .build();
        }

        private static RentLockerEvent getRentLockerEvent(final String eventType,
                                                          final LocalDateTime createDate,
                                                          final Integer sequenceNumber,
                                                          final RenterId renterId) {
            if (createDate == null || sequenceNumber == null || renterId == null) {
                throw JsonSerializerException.malformedEvent(MALFORMED_RENT_EVENT.formatted(eventType,
                                                                                            createDate,
                                                                                            sequenceNumber,
                                                                                            renterId));
            }
            return RentLockerEvent.builder()
                                  .createDate(createDate)
                                  .sequenceNumber(sequenceNumber)
                                  .renterId(renterId)
                                  .build();
        }

        private static SetUpLockerEvent getSetUpLockerEvent(final String eventType,
                                                            final LocalDateTime createDate,
                                                            final Integer sequenceNumber,
                                                            final LockerId lockerId) {
            if (createDate == null || sequenceNumber == null || lockerId == null) {
                throw JsonSerializerException.malformedEvent(MALFORMED_SET_UP_EVENT.formatted(eventType,
                                                                                              createDate,
                                                                                              sequenceNumber,
                                                                                              lockerId));
            }
            return SetUpLockerEvent.builder()
                                   .createDate(createDate)
                                   .sequenceNumber(sequenceNumber)
                                   .lockerId(lockerId)
                                   .build();
        }

    }

    @Override
    public JsonSerializedLockerEvent serialize(final IncomingEvent incomingEvent) {
        try {
            var json = objectMapper.writeValueAsString(incomingEvent);
            var event = new JsonSerializedLockerEvent();
            event.setContent(json);
            return event;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot serialize", e);
        }

    }

    @Override
    public IncomingEvent deserialize(final JsonSerializedLockerEvent lockerEvent) {
        var json = lockerEvent.getContent();

        try {
            return objectMapper.readValue(json, IncomingEvent.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot deserialize", e);
        }
    }

    @Override
    public JsonSerializedLockerEventStream createLockerEventStream(final UUID lockerId) {
        return new JsonSerializedLockerEventStream(lockerId);
    }

}
