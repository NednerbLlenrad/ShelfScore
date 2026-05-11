package learn.scoreshelf.data;

import learn.scoreshelf.data.mappers.GameSessionPlayerMapper;
import learn.scoreshelf.models.GameSessionPlayer;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameSessionPlayerRepositoryJdbcClient implements GameSessionPlayerRepository {

    private final JdbcClient jdbcClient;

    public GameSessionPlayerRepositoryJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<GameSessionPlayer> findAll() {

        final String sql = """
                select
                    game_session_player_id,
                    game_session_id,
                    player_id,
                    player_name,
                    total_score,
                    is_winner
                from game_session_player;
                """;

        return jdbcClient.sql(sql)
                .query(new GameSessionPlayerMapper())
                .list();
    }

    @Override
    public GameSessionPlayer findById(int gameSessionPlayerId) {

        final String sql = """
                select
                    game_session_player_id,
                    game_session_id,
                    player_id,
                    player_name,
                    total_score,
                    is_winner
                from game_session_player
                where game_session_player_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(gameSessionPlayerId)
                .query(new GameSessionPlayerMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public List<GameSessionPlayer> findByGameSessionId(int gameSessionId) {

        final String sql = """
                select
                    game_session_player_id,
                    game_session_id,
                    player_id,
                    player_name,
                    total_score,
                    is_winner
                from game_session_player
                where game_session_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(gameSessionId)
                .query(new GameSessionPlayerMapper())
                .list();
    }

    @Override
    public GameSessionPlayer add(GameSessionPlayer player) {

        final String sql = """
                insert into game_session_player (
                    game_session_id,
                    player_id,
                    player_name,
                    total_score,
                    is_winner
                )
                values (?, ?, ?, ?, ?);
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param(player.getGameSessionId())
                .param(player.getPlayerId())
                .param(player.getPlayerName())
                .param(player.getTotalScore())
                .param(player.getIsWinner())
                .update(keyHolder);

        player.setGameSessionPlayerId(keyHolder.getKey().intValue());

        return player;
    }

    @Override
    public boolean update(GameSessionPlayer player) {

        final String sql = """
                update game_session_player set
                    game_session_id = ?,
                    player_id = ?,
                    player_name = ?,
                    total_score = ?,
                    is_winner = ?
                where game_session_player_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(player.getGameSessionId())
                .param(player.getPlayerId())
                .param(player.getPlayerName())
                .param(player.getTotalScore())
                .param(player.getIsWinner())
                .param(player.getGameSessionPlayerId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int gameSessionPlayerId) {

        final String sql = """
                delete from game_session_player
                where game_session_player_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(gameSessionPlayerId)
                .update() > 0;
    }
}