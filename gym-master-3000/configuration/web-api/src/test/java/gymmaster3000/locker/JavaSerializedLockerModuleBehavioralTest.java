package gymmaster3000.locker;

import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import gymmaster3000.locker.adapter.out.javaserializer.JavaSerializedStreamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(SerializerProfile.JAVA_SERIALIZER)
public class JavaSerializedLockerModuleBehavioralTest extends AbstractLockerModuleBehavioralTest {

    @Autowired
    private JavaSerializedStreamRepository javaSerializedStreamRepository;

    @BeforeEach
    void clearDb() {
        javaSerializedStreamRepository.deleteAll();
    }

}