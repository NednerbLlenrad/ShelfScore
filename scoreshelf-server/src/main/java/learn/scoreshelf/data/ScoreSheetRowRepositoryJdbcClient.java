package learn.scoreshelf.data;

import learn.scoreshelf.data.mappers.ScoreSheetRowMapper;
import learn.scoreshelf.models.ScoreSheetRow;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScoreSheetRowRepositoryJdbcClient implements ScoreSheetRowRepository {

    private final JdbcClient jdbcClient;

    public ScoreSheetRowRepositoryJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<ScoreSheetRow> findAll() {

        final String sql = """
                select
                    score_sheet_row_id,
                    score_sheet_id,
                    row_name,
                    display_order,
                    row_type,
                    expression
                from score_sheet_row
                order by score_sheet_id, display_order;
                """;

        return jdbcClient.sql(sql)
                .query(new ScoreSheetRowMapper())
                .list();
    }

    @Override
    public ScoreSheetRow findById(int scoreSheetRowId) {

        final String sql = """
                select
                    score_sheet_row_id,
                    score_sheet_id,
                    row_name,
                    display_order,
                    row_type,
                    expression
                from score_sheet_row
                where score_sheet_row_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(scoreSheetRowId)
                .query(new ScoreSheetRowMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public List<ScoreSheetRow> findByScoreSheetId(int scoreSheetId) {

        final String sql = """
                select
                    score_sheet_row_id,
                    score_sheet_id,
                    row_name,
                    display_order,
                    row_type,
                    expression
                from score_sheet_row
                where score_sheet_id = ?
                order by display_order;
                """;

        return jdbcClient.sql(sql)
                .param(scoreSheetId)
                .query(new ScoreSheetRowMapper())
                .list();
    }

    @Override
    public ScoreSheetRow add(ScoreSheetRow row) {

        final String sql = """
                insert into score_sheet_row (
                    score_sheet_id,
                    row_name,
                    display_order,
                    row_type,
                    expression
                )
                values (?, ?, ?, ?, ?);
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param(row.getScoreSheetId())
                .param(row.getRowName())
                .param(row.getDisplayOrder())
                .param(row.getRowType().name())
                .param(row.getExpression())
                .update(keyHolder);

        row.setScoreSheetRowId(keyHolder.getKey().intValue());

        return row;
    }

    @Override
    public boolean update(ScoreSheetRow row) {

        final String sql = """
                update score_sheet_row set
                    score_sheet_id = ?,
                    row_name = ?,
                    display_order = ?,
                    row_type = ?,
                    expression = ?
                where score_sheet_row_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(row.getScoreSheetId())
                .param(row.getRowName())
                .param(row.getDisplayOrder())
                .param(row.getRowType().name())
                .param(row.getExpression())
                .param(row.getScoreSheetRowId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int scoreSheetRowId) {

        final String sql = """
                delete from score_sheet_row
                where score_sheet_row_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(scoreSheetRowId)
                .update() > 0;
    }
}