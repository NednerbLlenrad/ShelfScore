package learn.scoreshelf.controllers;

import learn.scoreshelf.domain.StatsService;
import learn.scoreshelf.models.Stats;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = {"http://localhost:5173"})
public class StatsController {

    private final StatsService service;

    public StatsController(StatsService service) {
        this.service = service;
    }

    @GetMapping("/{appUserId}")
    public List<Stats> findByAppUserId(@PathVariable int appUserId) {
        return service.findByAppUserId(appUserId);
    }
}