package gymmaster3000.locker.adapter.out.javaserializer;

import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Profile(SerializerProfile.JAVA_SERIALIZER)
public interface JavaSerializedStreamRepository extends JpaRepository<JavaSerializedLockerEventStream, UUID> {

}