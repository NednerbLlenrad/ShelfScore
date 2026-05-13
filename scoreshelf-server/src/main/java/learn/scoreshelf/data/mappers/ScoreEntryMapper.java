package learn.scoreshelf.data.mappers;

import learn.scoreshelf.models.ScoreEntry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreEntryMapper implements RowMapper<ScoreEntry> {

    @Override
    public ScoreEntry mapRow(ResultSet rs, int rowNum) throws SQLException {

        ScoreEntry scoreEntry = new ScoreEntry();

        scoreEntry.setScoreEntryId(rs.getInt("score_entry_id"));
        scoreEntry.setGameSessionPlayerId(rs.getInt("game_session_player_id"));
        scoreEntry.setScoreSheetRowId(rs.getInt("score_sheet_row_id"));
        scoreEntry.setValue(rs.getInt("value"));

        return scoreEntry;
    }
}