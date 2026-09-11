package roomescape;

public class ReservationTimeResponse {

    private final Long id;
    private final String time;

    public ReservationTimeResponse(Long id, String time) {
        this.id = id;
        this.time = time;
    }

    public static ReservationTimeResponse from(
            ReservationTime reservationTime
    ) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getTime()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}
