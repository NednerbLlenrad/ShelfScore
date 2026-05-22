package learn.scoreshelf.domain;

import learn.scoreshelf.data.StatsRepository;
import learn.scoreshelf.models.Stats;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService {

    private final StatsRepository repository;

    public StatsService(StatsRepository repository) {
        this.repository = repository;
    }

    public List<Stats> findByAppUserId(int appUserId) {
        return repository.findByAppUserId(appUserId);
    }
}