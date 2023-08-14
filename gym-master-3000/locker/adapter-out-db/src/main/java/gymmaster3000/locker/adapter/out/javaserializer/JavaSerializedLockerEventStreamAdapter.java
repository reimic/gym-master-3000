package gymmaster3000.locker.adapter.out.javaserializer;

import gymmaster3000.locker.adapter.out.db.LockerEventStreamPort;
import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.locker.domain.event.IncomingEvent;
import gymmaster3000.locker.domain.valueobject.LockerId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Profile(SerializerProfile.JAVA_SERIALIZER)
@RequiredArgsConstructor
public class JavaSerializedLockerEventStreamAdapter implements LockerEventStreamPort {

    private final EntityManager entityManager;
    private final JavaSerializedStreamRepository streamRepository;
    private final IncomingEventJavaSerializer eventSerializer;

    @Override
    public List<Locker> findAll() {
        return streamRepository.findAll()
                               .stream()
                               .map(this::lockAndMap)
                               .toList();
    }

    @Override
    public Optional<Locker> findBy(final @NonNull LockerId lockerId) {
        var eventStream = streamRepository.findById(lockerId.value());
        return eventStream.map(this::lockAndMap);
    }

    private Locker lockAndMap(JavaSerializedLockerEventStream eventStream) {
        entityManager.lock(eventStream, LockModeType.OPTIMISTIC);

        var events = eventStream.getEvents()
                                .stream()
                                .map(eventSerializer::deserialize)
                                .sorted(Comparator.comparing(IncomingEvent::getSequenceNumber))
                                .toList();

        return Locker.recreate(eventStream.getVersion(), events);
    }

    @Override
    public void save(final @NonNull Locker locker) {
        var lockerId = locker.getLockerId()
                             .value();

        var eventStream = streamRepository.findById(lockerId)
                                          .orElse(eventSerializer.createLockerEventStream(lockerId));

        if (!Objects.equals(eventStream.getVersion(), locker.getVersion())) {
            throw new OptimisticLockException();
        }

        var pendingEvents = locker.getAndClearPendingEvents()
                                  .stream()
                                  .map(eventSerializer::serialize)
                                  .toList();

        eventStream.getEvents()
                   .addAll(pendingEvents);

        streamRepository.save(eventStream);
    }

    @Override
    public void saveAll(final @NonNull List<Locker> lockers) {
        lockers.forEach(this::save);
    }

}
