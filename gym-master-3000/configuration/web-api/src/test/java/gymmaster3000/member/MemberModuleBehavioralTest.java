package gymmaster3000.member;

import gymmaster3000.configuration.GymMaster3000WebApiApplication;
import gymmaster3000.member.adapter.in.web.SignUpMemberRestAdapter;
import gymmaster3000.member.adapter.out.db.MemberRepository;
import gymmaster3000.testsconfiguration.BehavioralTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GymMaster3000WebApiApplication.class)
@AutoConfigureMockMvc
public class MemberModuleBehavioralTest {

    private static final String PATTERN_UUID = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    private static final String PATTERN_MEMBER_VIEW = "\\{\"memberId\":\"" + PATTERN_UUID + "\",\"memberEmail\":\"(.+)@(\\S+)\"}";
    private static final String PATTERN_MEMBER_VIEWS = "\\[(" + PATTERN_MEMBER_VIEW + "(, )?)?\\]";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    @AfterEach
    void clearDb() {
        memberRepository.deleteAll();
    }

    /**
     * Behavioral Test
     * <p>
     * The User wants to sign up for a gym membership.
     * <p></p>
     * Expected
     * <p>
     * The App should sign up the User and return their {@link java.util.UUID}.
     */
    @BehavioralTest
    void shouldSignUpNewMember() throws Exception {
        // given
        var email = "test@test";
        // when-then
        var result = mockMvc.perform(post("http://localhost:8080/api/v1/members/signup?memberEmail={memberEmail}",
                                          email)
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
     * The User wants to sign up for a gym membership, but the email they provide is already assigned to an existing account.
     * <p></p>
     * Expected
     * <p>
     * The App should not sign up the User and return a {@code 200 OK} status.
     * Could possibly link to an access retrieval page, if such is implemented.
     * Returning {@code 200 OK} instead of  {@code 409 Conflict} or any other status is a mean to protect the app from account enumeration.
     * <p>
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#name-200-ok">RFC 9110 HTTP Semantics: Section 15.3.1</a>
     */
    @BehavioralTest
    void shouldRefuseToSignUpANewMember_whenTheirEmailIsAssignedToAnExistingAccount() throws Exception {
        // given
        var email = "test@test";
        signUpMember(email);
        // when-then
        var result = mockMvc.perform(post("http://localhost:8080/api/v1/members/signup?memberEmail={memberEmail}",
                                          email)
                                             .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        assertThat(result)
                .isNotBlank()
                .matches(SignUpMemberRestAdapter.EMAIL_ALREADY_ASSIGNED.formatted(email));
    }

    /**
     * Behavioral Test
     * <p>
     * The User wants to sign up for a gym membership, and then verify that their uuid and provided email are correct.
     * <p></p>
     * Expected
     * <p>
     * The App should sign up the User. Then it should return a valid JSON member view with memberId and memberEmail.
     */
    @BehavioralTest
    void shouldSignUpNewMember_andQueryTheirDataById() throws Exception {
        // given
        var email = "test@test";
        // when-then
        var memberId = signUpMember(email);
        assertThat(memberId)
                .isNotBlank()
                .matches(PATTERN_UUID);
        // when-then
        var result = mockMvc.perform(get("http://localhost:8080/api/v1/members/find?memberId={memberId}",
                                         memberId)
                                             .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        assertThat(result)
                .isNotBlank()
                .matches(PATTERN_MEMBER_VIEW);
    }

    /**
     * Behavioral Test
     * <p>
     * The User wants to query the App for a list of all registered Members.
     * <p></p>
     * Expected
     * <p>
     * The App should return a list of valid JSON member views with memberId and memberEmail.
     */
    @BehavioralTest
    void shouldQueryAllMembers() throws Exception {
        // given
        var uuid1 = signUpMember("test1@test");
        var uuid2 = signUpMember("test2@test");
        var uuid3 = signUpMember("test3@test");
        // when-then
        String result = findAllMembers();
        assertThat(result)
                .isNotBlank()
                .matches(PATTERN_MEMBER_VIEWS)
                .contains(uuid1)
                .contains(uuid2)
                .contains(uuid3);
    }

    /**
     * Behavioral Test
     * <p>
     * The User wants to ensure the App has no registered Members. If that is the case they sign a new member and query their data.
     * <p></p>
     * Expected
     * <p>
     * The App should return an empty list. Then sign a new member. Then return a list of valid JSON member views with memberId and memberEmail with one member.
     * The uuid of in the member view should match the uuid provided during the aforementioned signing.
     */
    @BehavioralTest
    void shouldQueryAllMembers_andReturnEmptyList_whenNoMembersSigned_thenSignMember_AndQuery() throws Exception {
        // given
        var email = "test@test";
        // when-then
        var foundAtFirst = findAllMembers();
        assertThat(foundAtFirst)
                .isNotBlank()
                .matches("\\[]");
        // when-then
        var uuid = signUpMember(email);
        // when-then
        var foundLater = findAllMembers();
        assertThat(foundLater)
                .isNotBlank()
                .matches(PATTERN_MEMBER_VIEWS)
                .contains(uuid);
    }

    /**
     * Behavioral Test
     * <p>
     * The User is signed up for a gym membership for some time. They use several lockers, each a different number of times.
     * At some point they want to get recommendations for 3 most frequently used lockers, so that they can consider booking the same lockers as they usually do.
     * <p></p>
     * Then, they use a locker that was not on the list a number of times that would put it in top 3. However, they do it within one day, and there is no expectation the App will reflect this usage right away.
     * <p></p>
     * Expected
     * <p>
     * The App should return a list of 3 lockerIds in order from most frequently used to least used. Then it should allow the user to rent and release a different locker twice.
     * After that, when the user makes another call for recommendations it should return the same results as previous, ignoring the most recent rentings.
     */
    @BehavioralTest
    void shouldQueryForThreeMostFrequentlyUsedLockers_rentAndReleaseOtherLockers_andGetSameResultAsBefore() throws Exception {
        // given
        var email = "test@test";
        var memberId = signUpMember(email);
        var lockerId1 = setUpLocker();
        var lockerId2 = setUpLocker();
        var lockerId3 = setUpLocker();
        var lockerId4 = setUpLocker();
        rentAndReleaseLockerTimes(4, lockerId4, memberId);
        rentAndReleaseLockerTimes(3, lockerId3, memberId);
        rentAndReleaseLockerTimes(2, lockerId2, memberId);
        rentAndReleaseLockerTimes(1, lockerId1, memberId);
        // when-then
        var result = getPreferredLockers(memberId, 3);
        assertThat(result)
                .contains(lockerId4)
                .contains(lockerId3)
                .contains(lockerId2)
                .doesNotContain(lockerId1);
        // when-then
        rentAndReleaseLockerTimes(2, lockerId1, memberId);
        var resultAfterChange = getPreferredLockers(memberId, 3);
        assertThat(resultAfterChange)
                .contains(lockerId4)
                .contains(lockerId3)
                .contains(lockerId2)
                .doesNotContain(lockerId1);
    }

    /**
     * Utility method querying the App for all members via en endpoint.
     * <p>
     * Uses {@link MockMvc} for the request. Asserts appropriate status and return, if applicable.
     * <p>
     *
     * @return a list of member views as a string
     */
    private String findAllMembers() throws Exception {
        return mockMvc.perform(get("http://localhost:8080/api/v1/members/findAll")
                                       .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status().isOk())
                      .andReturn()
                      .getResponse()
                      .getContentAsString();
    }

    /**
     * Utility method persisting a member via en endpoint.
     * <p>
     * Uses {@link MockMvc} for the request. Asserts appropriate status and return, if applicable.
     * <p>
     *
     * @return a persisted member's UUID as a string
     */
    private String signUpMember(String email) throws Exception {
        return mockMvc.perform(post("http://localhost:8080/api/v1/members/signup?memberEmail={memberEmail}",
                                    email)
                                       .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status().isCreated())
                      .andReturn()
                      .getResponse()
                      .getContentAsString();
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
     * Utility method getting a list of N preferred lockers for a given member via an endpoint.
     * <p>
     * Uses {@link MockMvc} for the request. Asserts appropriate status and return, if applicable.
     */
    private String getPreferredLockers(final String memberId, final int count) throws Exception {
        return mockMvc.perform(get(
                              "http://localhost:8080/api/v1/members/preferred/lockers?memberId={memberId}&quantity={quantity}",
                              memberId,
                              count)
                                       .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status().isOk())
                      .andReturn()
                      .getResponse()
                      .getContentAsString();
    }

    /**
     * Utility method renting and releasing a locker a set number of times via an endpoint.
     * <p>
     * Uses {@link MockMvc} for the request. Asserts appropriate status and return, if applicable.
     */
    private void rentAndReleaseLockerTimes(final int count, final String lockerId, final String memberId) {
        IntStream.range(0, count)
                 .forEach(element -> {
                     try {
                         rentLocker(lockerId, memberId);
                         releaseLocker(lockerId);
                     } catch (Exception e) {
                         throw new RuntimeException(e);
                     }
                 });
    }

}
