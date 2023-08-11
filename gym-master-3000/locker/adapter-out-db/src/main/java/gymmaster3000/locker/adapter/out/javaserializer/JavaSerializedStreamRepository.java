package gymmaster3000.locker.adapter.out.javaserializer;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Profile("java-serializer")
public interface JavaSerializedStreamRepository extends JpaRepository<JavaSerializedLockerEventStream, UUID> {

}