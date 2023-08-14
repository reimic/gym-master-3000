package gymmaster3000.locker.domain.entity;

import gymmaster3000.locker.domain.event.ReleaseLockerEvent;
import gymmaster3000.locker.domain.event.RentLockerEvent;
import gymmaster3000.locker.domain.event.SetUpLockerEvent;
import gymmaster3000.locker.domain.valueobject.RenterId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static gymmaster3000.locker.domain.entity.LockerCurrentlyRentedException.LOCKER_CURRENTLY_RENTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LockerTest {

    @Test
    void shouldSetUpNewLocker() {
        // given-when
        var locker = Locker.setup();
        // then
        assertThat(locker.getLockerId()).isNotNull();
        assertThat(locker.isAvailable()).isTrue();
        assertThat(locker.getAndClearPendingEvents())
                .isNotNull()
                .hasSize(1)
                .allSatisfy(event -> assertThat(event).isInstanceOf(SetUpLockerEvent.class));
    }

    @Test
    void shouldRent_whenLockerAvailable() {
        // given
        var locker = Locker.setup();
        var renterId = RenterId.of(UUID.randomUUID());
        // when
        locker.rent(renterId);
        // then
        assertThat(locker.isRented()).isTrue();
        assertThat(locker.isRentedBy(renterId)).isTrue();
        assertThat(locker.getAndClearPendingEvents())
                .isNotNull()
                .hasSize(2)
                .anySatisfy(event -> assertThat(event).isInstanceOf(SetUpLockerEvent.class))
                .anySatisfy(event -> assertThat(event).isInstanceOf(RentLockerEvent.class));
    }

    @Test
    void shouldNotRent_whenLockerRented() {
        // given
        var locker = Locker.setup();
        var renterId = RenterId.of(UUID.randomUUID());
        var otherRenterId = RenterId.of(UUID.randomUUID());
        locker.rent(renterId);
        assertThat(locker.isRented()).isTrue();
        assertThat(locker.isRentedBy(renterId)).isTrue();
        // when-then
        assertThatThrownBy(() -> locker.rent(otherRenterId))
                .isInstanceOf(LockerCurrentlyRentedException.class)
                .hasMessage(LOCKER_CURRENTLY_RENTED.formatted(
                        locker.getLockerId()
                              .value()
                              .toString()));
        assertThat(locker.isRented()).isTrue();
        assertThat(locker.isRentedBy(renterId)).isTrue();
    }

    @Test
    void shouldRelease_whenLockerRented() {
        // given
        var locker = Locker.setup();
        var renterId = RenterId.of(UUID.randomUUID());
        locker.rent(renterId);
        assertThat(locker.isRented()).isTrue();
        assertThat(locker.isRentedBy(renterId)).isTrue();
        // when
        locker.release();
        // then
        assertThat(locker.isRented()).isFalse();
        assertThat(locker.isRentedBy(renterId)).isFalse();
        assertThat(locker.getAndClearPendingEvents())
                .isNotNull()
                .hasSize(3)
                .anySatisfy(event -> assertThat(event).isInstanceOf(SetUpLockerEvent.class))
                .anySatisfy(event -> assertThat(event).isInstanceOf(RentLockerEvent.class))
                .anySatisfy(event -> assertThat(event).isInstanceOf(ReleaseLockerEvent.class));
    }

    @Test
    void shouldNotThrowException_whenAskedToRelease_andLockerNotRented() {
        // given
        var locker = Locker.setup();
        assertThat(locker.isAvailable()).isTrue();
        // when
        locker.release();
        // then
        assertThat(locker.isAvailable()).isTrue();
    }

    @Test
    void shouldReturnTrue_whenAskedIsAvailable_whenLockerNotRented() {
        // given
        var locker = Locker.setup();
        assertThat(locker.getCurrentRenterId()).isEmpty();
        // when
        var result = locker.isAvailable();
        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalse_whenAskedIsAvailable_whenLockerRented() {
        // given
        var locker = Locker.setup();
        var renterId = RenterId.of(UUID.randomUUID());
        locker.rent(renterId);
        assertThat(locker.getCurrentRenterId()).isPresent();
        // when
        var result = locker.isAvailable();
        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrue_whenAskedIsRented_whenLockerNotRented() {
        // given
        var locker = Locker.setup();
        var renterId = RenterId.of(UUID.randomUUID());
        locker.rent(renterId);
        assertThat(locker.getCurrentRenterId()).isPresent();
        // when
        var result = locker.isRented();
        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalse_whenAskedIsRented_whenLockerNotRented() {
        // given
        var locker = Locker.setup();
        assertThat(locker.getCurrentRenterId()).isEmpty();
        // when
        var result = locker.isRented();
        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnOptionalWithRenterId_whenAskedForCurrentRenterId_andRented() {
        // given
        var locker = Locker.setup();
        var renterId = RenterId.of(UUID.randomUUID());
        locker.rent(renterId);
        assertThat(locker.isRented()).isTrue();
        // when
        var result = locker.getCurrentRenterId();
        // then
        assertThat(result)
                .isPresent()
                .hasValue(renterId);
    }

    @Test
    void shouldReturnEmptyOptional_whenAskedForCurrentRenterId_andNotRented() {
        // given
        var locker = Locker.setup();
        assertThat(locker.isRented()).isFalse();
        // when
        var result = locker.getCurrentRenterId();
        // then
        assertThat(result)
                .isEmpty();
    }

    @Test
    void shouldReturnTrue_whenAskedIfRentedBy_andRentedBy() {
        // given
        var locker = Locker.setup();
        var renterId = RenterId.of(UUID.randomUUID());
        locker.rent(renterId);
        assertThat(locker.getCurrentRenterId()).hasValue(renterId);
        // when
        var result = locker.isRentedBy(renterId);
        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalse_whenAskedIfRentedBy_andRentedBySomeoneElse() {
        // given
        var locker = Locker.setup();
        var otherRenterId = RenterId.of(UUID.randomUUID());
        locker.rent(otherRenterId);
        assertThat(locker.getCurrentRenterId()).hasValue(otherRenterId);
        var renterId = RenterId.of(UUID.randomUUID());
        // when
        var result = locker.isRentedBy(renterId);
        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalse_whenAskedIfRentedBy_andNotRented() {
        // given
        var locker = Locker.setup();
        var renterId = RenterId.of(UUID.randomUUID());
        assertThat(locker.isRented()).isFalse();
        // when
        var result = locker.isRentedBy(renterId);
        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnListOfPendingEvents_andClear() {
        // given
        var locker = Locker.setup();
        var renterId = RenterId.of(UUID.randomUUID());
        locker.rent(renterId);
        locker.release();
        // when
        var events = locker.getAndClearPendingEvents();
        // then
        assertThat(locker.getAndClearPendingEvents())
                .isNotNull()
                .isEmpty();
        assertThat(events)
                .isNotNull()
                .hasSize(3)
                .anySatisfy(event -> assertThat(event).isInstanceOf(SetUpLockerEvent.class))
                .anySatisfy(event -> assertThat(event).isInstanceOf(RentLockerEvent.class))
                .anySatisfy(event -> assertThat(event).isInstanceOf(ReleaseLockerEvent.class));

    }

}