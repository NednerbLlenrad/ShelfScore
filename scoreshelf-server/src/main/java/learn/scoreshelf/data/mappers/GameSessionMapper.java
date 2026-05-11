package learn.scoreshelf.data.mappers;

import learn.scoreshelf.models.GameSession;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameSessionMapper implements RowMapper<GameSession> {

    @Override
    public GameSession mapRow(ResultSet rs, int rowNum) throws SQLException {

        GameSession session = new GameSession();

        session.setGameSessionId(rs.getInt("game_session_id"));
        session.setGameId(rs.getInt("game_id"));
        session.setAppUserId(rs.getInt("app_user_id"));
        session.setPlayedAt(rs.getTimestamp("played_at").toLocalDateTime());

        return session;
    }
}