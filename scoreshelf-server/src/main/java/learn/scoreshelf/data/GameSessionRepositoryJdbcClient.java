package learn.scoreshelf.data;

import learn.scoreshelf.data.mappers.GameSessionMapper;
import learn.scoreshelf.models.GameSession;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameSessionRepositoryJdbcClient implements GameSessionRepository {

    private final JdbcClient jdbcClient;

    public GameSessionRepositoryJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<GameSession> findAll() {

        final String sql = """
                select
                    game_session_id,
                    game_id,
                    app_user_id,
                    played_at
                from game_session
                order by played_at desc;
                """;

        return jdbcClient.sql(sql)
                .query(new GameSessionMapper())
                .list();
    }

    @Override
    public GameSession findById(int gameSessionId) {

        final String sql = """
                select
                    game_session_id,
                    game_id,
                    app_user_id,
                    played_at
                from game_session
                where game_session_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(gameSessionId)
                .query(new GameSessionMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public List<GameSession> findByGameId(int gameId) {

        final String sql = """
                select
                    game_session_id,
                    game_id,
                    app_user_id,
                    played_at
                from game_session
                where game_id = ?
                order by played_at desc;
                """;

        return jdbcClient.sql(sql)
                .param(gameId)
                .query(new GameSessionMapper())
                .list();
    }

    @Override
    public List<GameSession> findByAppUserId(int appUserId) {

        final String sql = """
                select
                    game_session_id,
                    game_id,
                    app_user_id,
                    played_at
                from game_session
                where app_user_id = ?
                order by played_at desc;
                """;

        return jdbcClient.sql(sql)
                .param(appUserId)
                .query(new GameSessionMapper())
                .list();
    }

    @Override
    public GameSession add(GameSession session) {

        final String sql = """
                insert into game_session (
                    game_id,
                    app_user_id,
                    played_at
                )
                values (?, ?, ?);
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param(session.getGameId())
                .param(session.getAppUserId())
                .param(session.getPlayedAt())
                .update(keyHolder);

        session.setGameSessionId(keyHolder.getKey().intValue());

        return session;
    }

    @Override
    public boolean update(GameSession session) {

        final String sql = """
                update game_session set
                    game_id = ?,
                    app_user_id = ?,
                    played_at = ?
                where game_session_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(session.getGameId())
                .param(session.getAppUserId())
                .param(session.getPlayedAt())
                .param(session.getGameSessionId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int gameSessionId) {

        jdbcClient.sql("""
        delete se
        from score_entry se
        inner join game_session_player gsp
            on se.game_session_player_id = gsp.game_session_player_id
        where gsp.game_session_id = ?;
        """)
                .param(gameSessionId)
                .update();

        jdbcClient.sql("""
        delete from game_session_player
        where game_session_id = ?;
        """)
                .param(gameSessionId)
                .update();

        final String sql = """
        delete from game_session
        where game_session_id = ?;
        """;

        return jdbcClient.sql(sql)
                .param(gameSessionId)
                .update() > 0;
    }
}