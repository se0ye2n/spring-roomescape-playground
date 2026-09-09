package roomescape;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTime>> findAll() {
        return ResponseEntity.ok(timeService.findAll());
    }

    @PostMapping
    public ResponseEntity<ReservationTime> create(
            @Valid @RequestBody TimeRequest request
    ) {
        ReservationTime time = timeService.create(request);

        return ResponseEntity
                .created(URI.create("/times/" + time.getId()))
                .body(time);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
