package learn.scoreshelf.domain;

import learn.scoreshelf.data.ScoreEntryRepository;
import learn.scoreshelf.models.ScoreEntry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreEntryService {

    private final ScoreEntryRepository repository;

    public ScoreEntryService(ScoreEntryRepository repository) {
        this.repository = repository;
    }

    public List<ScoreEntry> findAll() {
        return repository.findAll();
    }

    public ScoreEntry findById(int scoreEntryId) {
        return repository.findById(scoreEntryId);
    }

    public List<ScoreEntry> findByGameSessionPlayerId(int gameSessionPlayerId) {
        return repository.findByGameSessionPlayerId(gameSessionPlayerId);
    }

    public Result<ScoreEntry> add(ScoreEntry scoreEntry) {

        Result<ScoreEntry> result = validate(scoreEntry);

        if (!result.isSuccess()) {
            return result;
        }

        if (scoreEntry.getScoreEntryId() != 0) {
            result.addMessage("Score entry ID cannot be set for add operation.", ResultType.INVALID);
            return result;
        }

        scoreEntry = repository.add(scoreEntry);
        result.setPayload(scoreEntry);

        return result;
    }

    public Result<ScoreEntry> update(ScoreEntry scoreEntry) {

        Result<ScoreEntry> result = validate(scoreEntry);

        if (!result.isSuccess()) {
            return result;
        }

        if (scoreEntry.getScoreEntryId() <= 0) {
            result.addMessage("Score entry ID is required for update.", ResultType.INVALID);
            return result;
        }

        if (!repository.update(scoreEntry)) {
            result.addMessage("Score entry not found.", ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int scoreEntryId) {
        return repository.deleteById(scoreEntryId);
    }

    //Helpers
    private Result<ScoreEntry> validate(ScoreEntry scoreEntry) {

        Result<ScoreEntry> result = new Result<>();

        if (scoreEntry == null) {
            result.addMessage("Score entry cannot be null.", ResultType.INVALID);
            return result;
        }

        if (scoreEntry.getGameSessionPlayerId() <= 0) {
            result.addMessage("Game session player ID is required.", ResultType.INVALID);
        }

        if (scoreEntry.getScoreSheetRowId() <= 0) {
            result.addMessage("Score sheet row ID is required.", ResultType.INVALID);
        }

        if (scoreEntry.getValue() < 0) {
            result.addMessage("Score value cannot be negative.", ResultType.INVALID);
        }

        return result;
    }
}