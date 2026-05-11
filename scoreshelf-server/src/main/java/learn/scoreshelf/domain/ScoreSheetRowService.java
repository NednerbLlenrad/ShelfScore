package learn.scoreshelf.domain;

import learn.scoreshelf.data.ScoreSheetRowRepository;
import learn.scoreshelf.models.ScoreSheetRow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreSheetRowService {

    private final ScoreSheetRowRepository repository;

    public ScoreSheetRowService(ScoreSheetRowRepository repository) {
        this.repository = repository;
    }

    public List<ScoreSheetRow> findAll() {
        return repository.findAll();
    }

    public ScoreSheetRow findById(int scoreSheetRowId) {
        return repository.findById(scoreSheetRowId);
    }

    public List<ScoreSheetRow> findByScoreSheetId(int scoreSheetId) {
        return repository.findByScoreSheetId(scoreSheetId);
    }

    public Result<ScoreSheetRow> add(ScoreSheetRow row) {

        Result<ScoreSheetRow> result = validate(row);

        if (!result.isSuccess()) {
            return result;
        }

        if (row.getScoreSheetRowId() != 0) {
            result.addMessage("Score sheet row ID cannot be set for add operation.", ResultType.INVALID);
            return result;
        }

        row = repository.add(row);
        result.setPayload(row);

        return result;
    }

    public Result<ScoreSheetRow> update(ScoreSheetRow row) {

        Result<ScoreSheetRow> result = validate(row);

        if (!result.isSuccess()) {
            return result;
        }

        if (row.getScoreSheetRowId() <= 0) {
            result.addMessage("Score sheet row ID is required for update.", ResultType.INVALID);
            return result;
        }

        if (!repository.update(row)) {
            result.addMessage("Score sheet row not found.", ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int scoreSheetRowId) {
        return repository.deleteById(scoreSheetRowId);
    }

    //Helpers
    private Result<ScoreSheetRow> validate(ScoreSheetRow row) {

        Result<ScoreSheetRow> result = new Result<>();

        if (row == null) {
            result.addMessage("Score sheet row cannot be null.", ResultType.INVALID);
            return result;
        }

        if (row.getScoreSheetId() <= 0) {
            result.addMessage("Score sheet ID is required.", ResultType.INVALID);
        }

        if (row.getRowName() == null || row.getRowName().isBlank()) {
            result.addMessage("Row name is required.", ResultType.INVALID);
        }

        if (row.getRowName() != null && row.getRowName().length() > 50) {
            result.addMessage("Row name must be 50 characters or less.", ResultType.INVALID);
        }

        if (row.getDisplayOrder() <= 0) {
            result.addMessage("Display order must be greater than 0.", ResultType.INVALID);
        }

        if (row.getRowType() == null) {
            result.addMessage("Row type is required.", ResultType.INVALID);
        }

        return result;
    }
}