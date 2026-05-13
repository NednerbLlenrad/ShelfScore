package learn.scoreshelf.data;

import learn.scoreshelf.data.mappers.ScoreEntryMapper;
import learn.scoreshelf.models.ScoreEntry;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScoreEntryRepositoryJdbcClient implements ScoreEntryRepository {

    private final JdbcClient jdbcClient;

    public ScoreEntryRepositoryJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<ScoreEntry> findAll() {

        final String sql = """
                select
                    score_entry_id,
                    game_session_player_id,
                    score_sheet_row_id,
                    value
                from score_entry;
                """;

        return jdbcClient.sql(sql)
                .query(new ScoreEntryMapper())
                .list();
    }

    @Override
    public ScoreEntry findById(int scoreEntryId) {

        final String sql = """
                select
                    score_entry_id,
                    game_session_player_id,
                    score_sheet_row_id,
                    value
                from score_entry
                where score_entry_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(scoreEntryId)
                .query(new ScoreEntryMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public List<ScoreEntry> findByGameSessionPlayerId(int gameSessionPlayerId) {

        final String sql = """
                select
                    score_entry_id,
                    game_session_player_id,
                    score_sheet_row_id,
                    value
                from score_entry
                where game_session_player_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(gameSessionPlayerId)
                .query(new ScoreEntryMapper())
                .list();
    }

    @Override
    public ScoreEntry add(ScoreEntry scoreEntry) {

        final String sql = """
                insert into score_entry (
                    game_session_player_id,
                    score_sheet_row_id,
                    value
                )
                values (?, ?, ?);
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param(scoreEntry.getGameSessionPlayerId())
                .param(scoreEntry.getScoreSheetRowId())
                .param(scoreEntry.getValue())
                .update(keyHolder);

        scoreEntry.setScoreEntryId(keyHolder.getKey().intValue());

        return scoreEntry;
    }

    @Override
    public boolean update(ScoreEntry scoreEntry) {

        final String sql = """
                update score_entry set
                    game_session_player_id = ?,
                    score_sheet_row_id = ?,
                    value = ?
                where score_entry_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(scoreEntry.getGameSessionPlayerId())
                .param(scoreEntry.getScoreSheetRowId())
                .param(scoreEntry.getValue())
                .param(scoreEntry.getScoreEntryId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int scoreEntryId) {

        final String sql = """
                delete from score_entry
                where score_entry_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(scoreEntryId)
                .update() > 0;
    }
}