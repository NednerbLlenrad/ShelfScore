package learn.scoreshelf.data;

import learn.scoreshelf.models.ScoreEntry;

import java.util.List;

public interface ScoreEntryRepository {

    List<ScoreEntry> findAll();

    ScoreEntry findById(int scoreEntryId);

    List<ScoreEntry> findByGameSessionPlayerId(int gameSessionPlayerId);

    ScoreEntry add(ScoreEntry scoreEntry);

    boolean update(ScoreEntry scoreEntry);

    boolean deleteById(int scoreEntryId);
}