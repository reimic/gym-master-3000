package gymmaster3000.locker;

import gymmaster3000.locker.adapter.out.db.SerializerProfile;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(SerializerProfile.JAVA_SERIALIZER)
public class JavaSerializedLockerModuleBehavioralTest extends AbstractLockerModuleBehavioralTest {

    public JavaSerializedLockerModuleBehavioralTest() {

    }

}