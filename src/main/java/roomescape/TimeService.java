package roomescape;

import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalTime;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<ReservationTime> findAll() {
        return timeRepository.findAll();
    }

    public ReservationTime create(TimeRequest request) {
        LocalTime time = LocalTime.parse(request.getTime());

        return timeRepository.save(time);
    }

    public void delete(Long id) {
        int deletedCount = timeRepository.delete(id);

        if (deletedCount == 0) {
            throw new NotFoundTimeException();
        }
    }
}