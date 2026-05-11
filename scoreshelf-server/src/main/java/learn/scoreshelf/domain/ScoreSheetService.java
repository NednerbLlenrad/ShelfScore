package learn.scoreshelf.domain;

import learn.scoreshelf.data.ScoreSheetRepository;
import learn.scoreshelf.models.ScoreSheet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreSheetService {

    private final ScoreSheetRepository repository;

    public ScoreSheetService(ScoreSheetRepository repository) {
        this.repository = repository;
    }

    public List<ScoreSheet> findAll(){
        return repository.findAll();
    }

    public ScoreSheet findById(int scoreSheetId){
        return repository.findById(scoreSheetId);
    }

    public Result<ScoreSheet> add(ScoreSheet scoreSheet) {

        Result<ScoreSheet> result = validate(scoreSheet);

        if (!result.isSuccess()) {
            return result;
        }

        if (scoreSheet.getScoreSheetId() != 0) {
            result.addMessage("ScoreSheet ID cannot be set for add operation.", ResultType.INVALID);
            return result;
        }

        scoreSheet = repository.add(scoreSheet);
        result.setPayload(scoreSheet);

        return result;
    }

    public Result<ScoreSheet> update(ScoreSheet scoreSheet) {

        Result<ScoreSheet> result = validate(scoreSheet);

        if (!result.isSuccess()) {
            return result;
        }

        if (scoreSheet.getScoreSheetId() <= 0) {
            result.addMessage("ScoreSheet ID is required for update.", ResultType.INVALID);
            return result;
        }

        if (!repository.update(scoreSheet)) {
            result.addMessage("ScoreSheet not found.", ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int scoreSheetId) {
        return repository.deleteById(scoreSheetId);
    }

    //Helpers
    private Result<ScoreSheet> validate(ScoreSheet scoreSheet) {

        Result<ScoreSheet> result = new Result<>();

        if (scoreSheet == null) {
            result.addMessage("ScoreSheet cannot be null.", ResultType.INVALID);
            return result;
        }

        if (scoreSheet.getGameId() <= 0) {
            result.addMessage("Game ID is required.", ResultType.INVALID);
        }

        if (scoreSheet.getScoreSheetName() == null || scoreSheet.getScoreSheetName().isBlank()) {
            result.addMessage("ScoreSheet name is required.", ResultType.INVALID);
        }

        if (scoreSheet.getScoreSheetName() != null && scoreSheet.getScoreSheetName().length() > 50) {
            result.addMessage("ScoreSheet name must be 50 characters or less.", ResultType.INVALID);
        }

        return result;
    }
}

