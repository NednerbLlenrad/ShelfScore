package learn.scoreshelf.data;

import learn.scoreshelf.models.ScoreSheetRow;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScoreSheetRowRepository {
    List<ScoreSheetRow> findAll();

    ScoreSheetRow findById(int scoreSheetRowId);

    List<ScoreSheetRow> findByScoreSheetId(int scoreSheetId);

    ScoreSheetRow add(ScoreSheetRow scoreSheetRow);

    boolean update(ScoreSheetRow scoreSheetRow);

    @Transactional
    boolean deleteById(int scoreSheetRowId);
}
