package learn.scoreshelf.data.mappers;

import learn.scoreshelf.models.Player;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerMapper implements RowMapper<Player> {

    @Override
    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {

        Player player = new Player();

        player.setPlayerId(rs.getInt("player_id"));
        player.setAppUserId(rs.getInt("app_user_id"));
        player.setPlayerName(rs.getString("player_name"));

        int linkedAppUserId = rs.getInt("linked_app_user_id");
        if (!rs.wasNull()) {
            player.setLinkedAppUserId(linkedAppUserId);
        }

        return player;
    }
}