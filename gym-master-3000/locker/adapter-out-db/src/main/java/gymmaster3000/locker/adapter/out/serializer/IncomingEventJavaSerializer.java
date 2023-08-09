package gymmaster3000.locker.adapter.out.serializer;

import gymmaster3000.locker.domain.event.IncomingEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Profile("java-serializer")
public class IncomingEventJavaSerializer implements IncomingEventSerializer {

    @Override
    public byte[] serialize(final IncomingEvent incomingEvent) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(incomingEvent);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot serialize", e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public IncomingEvent deserialize(final byte[] bytes) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (IncomingEvent) objectInputStream.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot deserialize", e);
        }
    }

}
