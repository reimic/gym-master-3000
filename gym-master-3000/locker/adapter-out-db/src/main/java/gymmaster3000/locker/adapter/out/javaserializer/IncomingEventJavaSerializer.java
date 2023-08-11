package gymmaster3000.locker.adapter.out.javaserializer;

import gymmaster3000.locker.adapter.out.db.IncomingEventSerializer;
import gymmaster3000.locker.domain.event.IncomingEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.UUID;

@Component
@Profile("java-serializer")
public class IncomingEventJavaSerializer implements IncomingEventSerializer<byte[], JavaSerializedLockerEvent> {

    @Override
    public JavaSerializedLockerEvent serialize(final IncomingEvent incomingEvent) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(incomingEvent);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot serialize", e);
        }
        var bytes = byteArrayOutputStream.toByteArray();
        var event = new JavaSerializedLockerEvent();
        event.setContent(bytes);
        return event;
    }

    @Override
    public IncomingEvent deserialize(final JavaSerializedLockerEvent lockerEvent) {
        var serialized = lockerEvent.getContent();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(serialized))) {
            return (IncomingEvent) objectInputStream.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot deserialize", e);
        }
    }

    @Override
    public JavaSerializedLockerEventStream createLockerEventStream(final UUID lockerId) {
        return new JavaSerializedLockerEventStream(lockerId);
    }

}
