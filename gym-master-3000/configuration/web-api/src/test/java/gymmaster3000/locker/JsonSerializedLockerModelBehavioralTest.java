package gymmaster3000.locker;

import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import gymmaster3000.locker.adapter.out.jsonserializer.JsonSerializedStreamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(SerializerProfile.JSON_SERIALIZER)
public class JsonSerializedLockerModelBehavioralTest extends AbstractLockerModuleBehavioralTest {

    @Autowired
    private JsonSerializedStreamRepository jsonSerializedStreamRepository;

    @BeforeEach
    void clearDb() {
        jsonSerializedStreamRepository.deleteAll();
    }

}
