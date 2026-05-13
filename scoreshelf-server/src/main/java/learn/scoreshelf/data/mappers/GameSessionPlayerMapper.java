package learn.scoreshelf.data.mappers;

import learn.scoreshelf.models.GameSessionPlayer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameSessionPlayerMapper implements RowMapper<GameSessionPlayer> {

    @Override
    public GameSessionPlayer mapRow(ResultSet rs, int rowNum) throws SQLException {

        GameSessionPlayer player = new GameSessionPlayer();

        player.setGameSessionPlayerId(rs.getInt("game_session_player_id"));
        player.setGameSessionId(rs.getInt("game_session_id"));

        int playerId = rs.getInt("player_id");
        if (!rs.wasNull()) {
            player.setPlayerId(playerId);
        }

        player.setPlayerName(rs.getString("player_name"));
        player.setTotalScore(rs.getInt("total_score"));
        player.setIsWinner(rs.getBoolean("is_winner"));

        return player;
    }
}