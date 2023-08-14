package gymmaster3000.locker;

import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(SerializerProfile.JSON_SERIALIZER)
public class JsonSerializedLockerModelBehavioralTest extends AbstractLockerModuleBehavioralTest {

    public JsonSerializedLockerModelBehavioralTest() {
        super();
    }

}
