package roomescape;

import org.springframework.stereotype.Service;

import java.util.List;

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
        return timeRepository.save(request.getTime());
    }

    public void delete(Long id) {
        int deletedCount = timeRepository.delete(id);

        if (deletedCount == 0) {
            throw new NotFoundTimeException();
        }
    }
}