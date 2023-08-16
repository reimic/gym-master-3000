package gymmaster3000.locker;

import gymmaster3000.configuration.GymMaster3000WebApiApplication;
import gymmaster3000.locker.domain.entity.LockerCurrentlyRentedException;
import gymmaster3000.locker.domain.entity.LockerNotFoundException;
import gymmaster3000.testsconfiguration.BehavioralTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GymMaster3000WebApiApplication.class)
@AutoConfigureMockMvc
abstract class AbstractLockerModuleBehavioralTest {

    protected static final String PATTERN_UUID = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    @Autowired
    protected MockMvc mockMvc;

    /**
     * Behavioral Test
     * <p>
     * The User wants to set up a new locker.
     * <p></p>
     * Expected
     * <p>
     * The App should set up a new locker and return its {@link UUID}.
     */
    @BehavioralTest
    protected void shouldSetUpLocker() throws Exception {
        // given-when-then
        var result = mockMvc.perform(post("http://localhost:8080/api/v1/lockers/setup")
                                             .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isCreated())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        assertThat(result)
                .isNotBlank()
                .matches(PATTERN_UUID);
    }

    /**
     * Behavioral Test
     * <p>
     * The User wants to rent a locker. Knows the lockerId and theirs renterId.
     * <p></p>
     * Expected
     * <p>
     * The App should rent the locker to the User.
     */
    @BehavioralTest
    protected void shouldRentALocker() throws Exception {
        // given
        String lockerId = setUpLocker();
        var renterId = getRandomStringUUID();
        // when-then
        rentLocker(lockerId, renterId);
        var isRentedByRenter = isRentedByRenter(lockerId, renterId);
        assertThat(isRentedByRenter).isTrue();
    }

    /**
     * Behavioral Test
     * <p>
     * The User currently rents a locker and wants to release it.
     * <p></p>
     * Expected
     * <p>
     * The App should release the locker.
     */
    @BehavioralTest
    protected void shouldReleaseALocker() throws Exception {
        // given
        var lockerId = setUpLocker();
        var renterId = getRandomStringUUID();
        rentLocker(lockerId, renterId);
        boolean isRentedByRenter = isRentedByRenter(lockerId, renterId);
        assertThat(isRentedByRenter).isTrue();
        // when-then
        releaseLocker(lockerId);
        boolean isRentedByRenterAfterRelease = isRentedByRenter(lockerId, renterId);
        assertThat(isRentedByRenterAfterRelease).isFalse();
    }

    /**
     * Behavioral Test
     * <p>
     * The User thinks they rent a locker and want to release it, but a wrong lockerId is provided.
     * <p></p>
     * Expected
     * <p>
     * The App should return {@code 404 Not Found} status for a request to release a non-existent locker.
     * <p>
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#name-404-not-found">RFC 9110 HTTP Semantics: Section 15.5.5</a>
     */
    @BehavioralTest
    protected void shouldRefuseToReleaseALocker_whenItDoesNotExist() throws Exception {
        // given
        var lockerId = getRandomStringUUID();
        // when-then
        var response = mockMvc
                .perform(post("http://localhost:8080/api/v1/lockers/release?lockerId={lockerId}", lockerId))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(response)
                .isNotBlank()
                .isEqualTo(LockerNotFoundException.LOCKER_NOT_FOUND.formatted(lockerId));
    }

    /**
     * Behavioral Test
     * <p>
     * The User wants to rent a locker. Knows a lockerId and renterId. Then the User wants to release the locker.
     */
    @BehavioralTest
    protected void shouldRentALocker_andThenReleaseIt() throws Exception {
        // given
        var lockerId = setUpLocker();
        var renterId = getRandomStringUUID();
        // when-then
        rentLocker(lockerId, renterId);
        boolean isRentedByRenter = isRentedByRenter(lockerId, renterId);
        assertThat(isRentedByRenter).isTrue();
        // when-then
        releaseLocker(lockerId);
        boolean isRentedByRenterAfterRelease = isRentedByRenter(lockerId, renterId);
        assertThat(isRentedByRenterAfterRelease).isFalse();
    }

    /**
     * Behavioral Test
     * <p>
     * The User wants to rent a locker. Knows a lockerId and renterId. But that particular locker is already rented.
     * <p></p>
     * Expected
     * <p>
     * The App should return {@code 409 Conflict} status for a request to rent an already rented locker.
     * <p>
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#name-409-conflict">RFC 9110 HTTP Semantics: Section 15.5.10</a>
     */
    @BehavioralTest
    protected void shouldRefuseToRentALocker_whenItIsRented() throws Exception {
        // given
        var lockerId = setUpLocker();
        var otherRenterId = getRandomStringUUID();
        rentLocker(lockerId, otherRenterId);
        isRentedByRenter(lockerId, otherRenterId);
        var renterId = getRandomStringUUID();
        var request = """
                      {
                      "lockerId": "%s",
                      "renterId": "%s"
                      }
                      """.formatted(lockerId, renterId);
        // when-then
        var response = mockMvc
                .perform(post("http://localhost:8080/api/v1/lockers/rent")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(request))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response)
                .isNotBlank()
                .isEqualTo(LockerCurrentlyRentedException.LOCKER_CURRENTLY_RENTED.formatted(lockerId));
    }

    /**
     * Behavioral Test
     * <p>
     * The User wants to rent a locker, but the provided lockerId does not represent any locker in the system.
     * <p></p>
     * Expected
     * <p>
     * The App should return {@code 404 Not Found} status for a request to rent a non-existent locker.
     * <p>
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#name-404-not-found">RFC 9110 HTTP Semantics: Section 15.5.5</a>
     */
    @BehavioralTest
    protected void shouldRefuseToRentALocker_whenItDoesNotExist() throws Exception {
        // given
        var lockerId = getRandomStringUUID();
        var renterId = getRandomStringUUID();
        var request = """
                      {
                      "lockerId": "%s",
                      "renterId": "%s"
                      }
                      """.formatted(lockerId, renterId);
        // when-then
        var response = mockMvc
                .perform(post("http://localhost:8080/api/v1/lockers/rent")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(request))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(response)
                .isNotBlank()
                .isEqualTo(LockerNotFoundException.LOCKER_NOT_FOUND.formatted(lockerId));

    }

    /**
     * Behavioral Test
     * <p>
     * The User wants to release all lockers rented by a given renter and knows theirs renterId.
     * <p></p>
     * Expected
     * <p>
     * The App should release all lockers currently being rented by the given renter.
     */
    @BehavioralTest
    protected void shouldReleaseAllLockersRentedByRenter() throws Exception {
        // given
        var renterId = getRandomStringUUID();
        var lockerId_1 = setUpLocker();
        rentLocker(lockerId_1, renterId);
        var lockerId_2 = setUpLocker();
        rentLocker(lockerId_2, renterId);
        var isRentedByRenter_1 = isRentedByRenter(lockerId_1, renterId);
        assertThat(isRentedByRenter_1).isTrue();
        var isRentedByRenter_2 = isRentedByRenter(lockerId_2, renterId);
        assertThat(isRentedByRenter_2).isTrue();
        // when-then
        releaseAllLockersBy(renterId);
        var isRentedByRenterAfterRelease_1 = isRentedByRenter(lockerId_1, renterId);
        assertThat(isRentedByRenterAfterRelease_1).isFalse();
        var isRentedByRenterAfterRelease_2 = isRentedByRenter(lockerId_1, renterId);
        assertThat(isRentedByRenterAfterRelease_2).isFalse();
    }

    /**
     * Behavioral Test
     * <p>
     * The User wants to release all lockers rented by a given renter and provides a renterId that is not assigned to any currently renter locker.
     * <p></p>
     * Expected
     * <p>
     * The App should return {@code 200 OK} status for a request to release all lockers of a renter, who is not renting any lockers at this point in time.
     * It is paramount the App never returns an error for such a request, since the state of the App matches the Users expectation - no lockers are rented by a renter with the provided renterId.
     * <p>
     * The Locker module does not concern itself with the validity of the renterId, beyond its syntactic form.
     * <p>
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#name-200-ok">RFC 9110 HTTP Semantics: Section 15.3.1</a>
     */
    @BehavioralTest
    protected void shouldNotSignalAnyError_whenAttemptingToReleaseAllLockersRentedByRenter_andRenterDoesNotRentAnything()
            throws Exception {
        // given
        var renterId = getRandomStringUUID();
        var lockerId = setUpLocker();
        var isRentedByRenter = isRentedByRenter(lockerId, renterId);
        assertThat(isRentedByRenter).isFalse();
        // when-then
        releaseAllLockersBy(renterId);
        var isRentedByRenterAfterRelease = isRentedByRenter(lockerId, renterId);
        assertThat(isRentedByRenterAfterRelease).isFalse();
    }

    // ---- Utility Methods ----

    /**
     * Utility method returning a random UUID in string form.
     * <p>
     *
     * @return random {@link UUID} as a String
     */
    private static String getRandomStringUUID() {
        return UUID
                .randomUUID()
                .toString();
    }

    /**
     * Utility method persisting a locker via en endpoint.
     * <p>
     * Uses {@link MockMvc} for the request. Asserts appropriate status and return, if applicable.
     * <p>
     *
     * @return a persisted locker's UUID as a string
     */
    private String setUpLocker() throws Exception {
        var lockerId = mockMvc
                .perform(post("http://localhost:8080/api/v1/lockers/setup")
                                 .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(lockerId)
                .isNotBlank()
                .matches(PATTERN_UUID);
        return lockerId;
    }

    /**
     * Utility method renting a locker via en endpoint.
     * <p>
     * Uses {@link MockMvc} for the request. Asserts appropriate status and return, if applicable.
     */
    private void rentLocker(final String lockerId, final String renterId) throws Exception {
        var request = """
                      {
                      "lockerId": "%s",
                      "renterId": "%s"
                      }
                      """.formatted(lockerId, renterId);
        mockMvc
                .perform(post("http://localhost:8080/api/v1/lockers/rent")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(request))
                .andExpect(status().isOk());
    }

    /**
     * Utility method releasing a locker via an endpoint.
     * <p>
     * Uses {@link MockMvc} for the request. Asserts appropriate status and return, if applicable.
     */
    private void releaseLocker(final String lockerId) throws Exception {
        mockMvc
                .perform(post("http://localhost:8080/api/v1/lockers/release?lockerId={lockerId}", lockerId))
                .andExpect(status().isOk());
    }

    /**
     * Utility method verifying a given locker is rented by a given renter via en endpoint.
     * <p>
     * Uses {@link MockMvc} for the request. Asserts appropriate status and return, if applicable.
     * <p>
     *
     * @return a boolean, true if the locker is in fact rented by that particular renter, false otherwise
     */
    private boolean isRentedByRenter(final String lockerId, final String renterId) throws Exception {
        var isRentedByResponse = mockMvc
                .perform(get(
                        "http://localhost:8080/api/v1/lockers/find/isrentedby?lockerId={lockerId}&renterId={renterId}",
                        lockerId, renterId))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(isRentedByResponse).isNotBlank();
        return Boolean.parseBoolean(isRentedByResponse);
    }

    /**
     * Utility method releasing all lockers rented by given renter via an endpoint.
     * <p>
     * Uses {@link MockMvc} for the request. Asserts appropriate status and return, if applicable.
     */
    private void releaseAllLockersBy(final String renterId) throws Exception {
        mockMvc.perform(post("http://localhost:8080/api/v1/lockers/releaseAll?renterId={renterId}", renterId))
               .andExpect(status().isOk());
    }

}
