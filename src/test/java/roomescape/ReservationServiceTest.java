package roomescape;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2026, 9, 6, 10, 0)
                        .atZone(zone)
                        .toInstant(),
                zone
        );

        reservationRepository = mock(ReservationRepository.class);
        timeRepository = mock(TimeRepository.class);

        reservationService = new ReservationService(
                reservationRepository,
                timeRepository,
                fixedClock
        );
    }

    private void 시간등록(String time) {
        when(timeRepository.findById(1L))
                .thenReturn(Optional.of(new ReservationTime(1L, time)));
    }

    @Test
    void 과거_시각의_예약은_거부한다() {
        시간등록("09:59");
        ReservationRequest request =
                new ReservationRequest("브라운", "2026-09-06", 1L);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(InvalidReservationException.class);

        verifyNoInteractions(reservationRepository);
    }

    @Test
    void 현재와_같은_시각의_예약은_거부한다() {
        시간등록("10:00");
        ReservationRequest request =
                new ReservationRequest("브라운", "2026-09-06", 1L);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(InvalidReservationException.class);

        verifyNoInteractions(reservationRepository);
    }

    @Test
    void 미래_시각의_예약은_저장한다() {
        시간등록("10:01");
        ReservationRequest request =
                new ReservationRequest("브라운", "2026-09-06", 1L);

        reservationService.create(request);

        ArgumentCaptor<Reservation> captor =
                ArgumentCaptor.forClass(Reservation.class);

        verify(reservationRepository).save(captor.capture());

        Reservation saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("브라운");
        assertThat(saved.getDate()).isEqualTo("2026-09-06");
        assertThat(saved.getTime().getId()).isEqualTo(1L);
        assertThat(saved.getTime().getTime()).isEqualTo("10:01");
    }

    @Test
    void 과거_시각으로_예약을_수정할_수_없다() {
        시간등록("09:59");
        ReservationRequest request =
                new ReservationRequest("브라운", "2026-09-06", 1L);

        assertThatThrownBy(() -> reservationService.update(1L, request))
                .isInstanceOf(InvalidReservationException.class);

        verifyNoInteractions(reservationRepository);
    }

    @Test
    void 존재하지_않는_시간으로_예약할_수_없다() {
        when(timeRepository.findById(999L))
                .thenReturn(Optional.empty());

        ReservationRequest request =
                new ReservationRequest("브라운", "2026-09-07", 999L);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(InvalidReservationException.class);

        verifyNoInteractions(reservationRepository);
    }
}