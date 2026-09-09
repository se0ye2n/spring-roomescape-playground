package roomescape;

public class ReservationTime {

    private final Long id;
    private final String time;

    public ReservationTime(Long id, String time) {
        this.id = id;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}
