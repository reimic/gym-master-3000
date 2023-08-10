package gymmaster3000.locker.application.usecase;

import gymmaster3000.locker.domain.entity.Locker;
import gymmaster3000.locker.domain.event.IncomingEvent;
import gymmaster3000.locker.domain.event.SetupLockerEvent;
import gymmaster3000.locker.application.port.SaveLockerPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static gymmaster3000.locker.application.usecase.SetUpLockerUseCase.SetUpLockerCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SetUpLockerUseCaseTest {

    @InjectMocks
    private SetUpLockerUseCase testedObject;

    @Mock
    private SaveLockerPort saveLockerPort;

    @Test
    void shouldSetUpLocker() {
        // given
        var command = new SetUpLockerCommand();
        doAnswer(invocation -> {
            var argument = invocation.getArgument(0, Locker.class);
            assertThat(argument.getLockerId()).isNotNull();
            Field pendingEvents = argument.getClass()
                                          .getDeclaredField("pendingEvents");
            pendingEvents.setAccessible(true);
            List<IncomingEvent> events = (List<IncomingEvent>) pendingEvents.get(argument);
            assertThat(events).isNotNull()
                              .hasSize(1);
            assertThat(events.get(0)).isInstanceOf(SetupLockerEvent.class);
            assertThat(argument.getCurrentRenterId()).isEmpty();
            return null;
        }).when(saveLockerPort)
          .save(any(Locker.class));

        // when
        var result = testedObject.apply(command);

        // then
        assertThat(result).isNotNull()
                          .isInstanceOf(UUID.class);
        verify(saveLockerPort).save(any(Locker.class));
    }

}