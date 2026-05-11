package learn.scoreshelf.data.mappers;

import learn.scoreshelf.models.ScoreSheet;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreSheetMapper implements RowMapper<ScoreSheet> {

    @Override
    public ScoreSheet mapRow(ResultSet rs, int rowNum) throws SQLException {
        ScoreSheet scoreSheet = new ScoreSheet();

        scoreSheet.setScoreSheetId(rs.getInt("score_sheet_id"));
        scoreSheet.setGameId(rs.getInt("game_id"));
        scoreSheet.setScoreSheetName(rs.getString("score_sheet_name"));

        return scoreSheet;
    }
}