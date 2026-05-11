package learn.scoreshelf.data.mappers;

import learn.scoreshelf.models.Game;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements RowMapper<Game> {

    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {

        Game game = new Game();

        game.setGameId(rs.getInt("game_id"));
        game.setGameName(rs.getString("game_name"));
        game.setImageUrl(rs.getString("image_url"));
        game.setCategory(rs.getString("category"));
        game.setMinPlayers(rs.getInt("min_players"));
        game.setMaxPlayers(rs.getInt("max_players"));
        game.setPrivate(rs.getBoolean("is_private"));
        game.setAppUserId(rs.getInt("app_user_id"));

        return game;
    }
}