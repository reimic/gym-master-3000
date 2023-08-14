package gymmaster3000.locker.adapter.out.jsonserializer;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Profile("json-serializer")
public interface JsonSerializedStreamRepository extends JpaRepository<JsonSerializedLockerEventStream, UUID> {

}
