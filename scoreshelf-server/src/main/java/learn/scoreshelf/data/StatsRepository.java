package learn.scoreshelf.data;

import learn.scoreshelf.models.Stats;

import java.util.List;

public interface StatsRepository {
    List<Stats> findAll();
}