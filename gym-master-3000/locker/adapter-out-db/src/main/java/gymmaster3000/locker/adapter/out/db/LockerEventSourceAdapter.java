package gymmaster3000.locker.adapter.out.db;

import gymmaster3000.locker.adapter.out.serializer.IncomingEventSerializer;
import gymmaster3000.locker.adapter.out.serializer.LockerEvent;
import gymmaster3000.locker.adapter.out.serializer.LockerEventStream;
import gymmaster3000.locker.adapter.out.serializer.LockerEventStreamRepository;
import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.locker.domain.event.IncomingEvent;
import gymmaster3000.locker.domain.valueobject.LockerId;
import gymmaster3000.member.application.port.FindAllLockersPort;
import gymmaster3000.member.application.port.FindLockerPort;
import gymmaster3000.member.application.port.SaveAllLockersPort;
import gymmaster3000.member.application.port.SaveLockerPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class LockerEventSourceAdapter implements SaveLockerPort,
                                                 SaveAllLockersPort,
                                                 FindLockerPort,
                                                 FindAllLockersPort {

    private final EntityManager entityManager;
    private final LockerEventStreamRepository streamRepository;
    private final IncomingEventSerializer eventSerializer;

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

    @Override
    public void save(final @NonNull Locker locker) {
        var lockerId = locker.getLockerId()
                             .value();

        var eventStream = streamRepository.findById(lockerId)
                                          .orElseGet(() -> {
                                              var newStream = new LockerEventStream();
                                              newStream.setEvents(new HashSet<>());
                                              newStream.setLockerId(lockerId);
                                              return newStream;
                                          });

        if (!Objects.equals(eventStream.getVersion(), locker.getVersion())) {
            // TODO Consider a custom error with a specific error message
            throw new OptimisticLockException();
        }

        var pendingEvents = locker.getAndClearPendingEvents()
                                  .stream()
                                  .map(eventSerializer::serialize)
                                  .map(bytes -> {
                                      var event = new LockerEvent();
                                      event.setContent(bytes);
                                      return event;
                                  })
                                  .toList();

        eventStream.getEvents()
                   .addAll(pendingEvents);

        streamRepository.save(eventStream);
    }

    @Override
    public void saveAll(final @NonNull List<Locker> lockers) {
        lockers.forEach(this::save);
    }

    private Locker lockAndMap(LockerEventStream eventStream) {
        entityManager.lock(eventStream, LockModeType.OPTIMISTIC);

        var events = eventStream.getEvents()
                                .stream()
                                .map(LockerEvent::getContent)
                                .map(eventSerializer::deserialize)
                                .sorted(Comparator.comparingInt(IncomingEvent::getSequenceNumber))
                                .toList();

        return Locker.recreate(events, eventStream.getVersion());
    }

}
