package gymmaster3000.locker.adapter.out.jsonserializer;

import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Profile(SerializerProfile.JSON_SERIALIZER)
public interface JsonSerializedStreamRepository extends JpaRepository<JsonSerializedLockerEventStream, UUID> {

}
