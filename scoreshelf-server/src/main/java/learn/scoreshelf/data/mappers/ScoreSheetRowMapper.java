package learn.scoreshelf.data.mappers;

import learn.scoreshelf.models.RowType;
import learn.scoreshelf.models.ScoreSheetRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreSheetRowMapper implements RowMapper<ScoreSheetRow> {

    @Override
    public ScoreSheetRow mapRow(ResultSet rs, int rowNum) throws SQLException {

        ScoreSheetRow row = new ScoreSheetRow();

        row.setScoreSheetRowId(rs.getInt("score_sheet_row_id"));
        row.setScoreSheetId(rs.getInt("score_sheet_id"));
        row.setRowName(rs.getString("row_name"));
        row.setDisplayOrder(rs.getInt("display_order"));

        row.setRowType(
                RowType.valueOf(rs.getString("row_type"))
        );

        row.setExpression(rs.getString("expression"));

        return row;
    }
}