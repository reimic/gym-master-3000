package gymmaster3000.locker.adapter.out.serializer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LockerEventStreamRepository extends JpaRepository<LockerEventStream, UUID> {

}
