package learn.scoreshelf.data;

import learn.scoreshelf.models.Stats;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatsRepositoryJdbcClient implements StatsRepository {

    private final JdbcClient jdbcClient;

    public StatsRepositoryJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Stats> findAll() {
        final String sql = """
                select
                    g.game_name,
                    gsp.player_name,
                    count(*) as play_count,
                    sum(case when gsp.is_winner = true then 1 else 0 end) as win_count
                from game_session gs
                inner join game g on gs.game_id = g.game_id
                inner join game_session_player gsp on gs.game_session_id = gsp.game_session_id
                group by g.game_name, gsp.player_name
                order by g.game_name, win_count desc, play_count desc;
                """;

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    Stats stats = new Stats();

                    stats.setGameName(rs.getString("game_name"));
                    stats.setPlayerName(rs.getString("player_name"));
                    stats.setPlayCount(rs.getInt("play_count"));
                    stats.setWinCount(rs.getInt("win_count"));

                    return stats;
                })
                .list();
    }
}