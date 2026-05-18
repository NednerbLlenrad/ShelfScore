package learn.scoreshelf.data;

import learn.scoreshelf.models.ScoreSheet;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScoreSheetRepository {
    List<ScoreSheet> findAll();

    ScoreSheet findById(int scoreSheetId);

    List<ScoreSheet> findByGameId(int gameId);

    ScoreSheet add(ScoreSheet scoreSheet);

    boolean update(ScoreSheet scoreSheet);

    @Transactional
    boolean deleteById(int scoreSheetId);
}
