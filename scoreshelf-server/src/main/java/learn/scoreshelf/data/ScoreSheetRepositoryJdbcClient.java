package learn.scoreshelf.data;

import learn.scoreshelf.data.mappers.ScoreSheetMapper;
import learn.scoreshelf.models.ScoreSheet;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScoreSheetRepositoryJdbcClient implements ScoreSheetRepository {

    private final JdbcClient jdbcClient;

    public ScoreSheetRepositoryJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<ScoreSheet> findAll() {

        final String sql = """
                select
                    score_sheet_id,
                    game_id,
                    score_sheet_name
                from score_sheet;
                """;

        return jdbcClient.sql(sql)
                .query(new ScoreSheetMapper())
                .list();
    }

    @Override
    public ScoreSheet findById(int scoreSheetId) {

        final String sql = """
                select
                    score_sheet_id,
                    game_id,
                    score_sheet_name
                from score_sheet
                where score_sheet_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(scoreSheetId)
                .query(new ScoreSheetMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public ScoreSheet add(ScoreSheet scoreSheet) {

        final String sql = """
                insert into score_sheet (
                    game_id,
                    score_sheet_name
                )
                values (?, ?);
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param(scoreSheet.getGameId())
                .param(scoreSheet.getScoreSheetName())
                .update(keyHolder);

        scoreSheet.setScoreSheetId(keyHolder.getKey().intValue());

        return scoreSheet;
    }

    @Override
    public boolean update(ScoreSheet scoreSheet) {

        final String sql = """
                update score_sheet set
                    game_id = ?,
                    score_sheet_name = ?
                where score_sheet_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(scoreSheet.getGameId())
                .param(scoreSheet.getScoreSheetName())
                .param(scoreSheet.getScoreSheetId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int scoreSheetId) {

        final String sql = """
                delete from score_sheet
                where score_sheet_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(scoreSheetId)
                .update() > 0;
    }
}