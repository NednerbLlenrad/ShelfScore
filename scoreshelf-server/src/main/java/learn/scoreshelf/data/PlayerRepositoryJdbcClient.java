package learn.scoreshelf.data;

import learn.scoreshelf.data.mappers.PlayerMapper;
import learn.scoreshelf.models.Player;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerRepositoryJdbcClient implements PlayerRepository {

    private final JdbcClient jdbcClient;

    public PlayerRepositoryJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Player> findAll() {

        final String sql = """
                select
                    player_id,
                    app_user_id,
                    player_name,
                    linked_app_user_id
                from player;
                """;

        return jdbcClient.sql(sql)
                .query(new PlayerMapper())
                .list();
    }

    @Override
    public Player findById(int playerId) {

        final String sql = """
                select
                    player_id,
                    app_user_id,
                    player_name,
                    linked_app_user_id
                from player
                where player_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(playerId)
                .query(new PlayerMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public List<Player> findByAppUserId(int appUserId) {

        final String sql = """
                select
                    player_id,
                    app_user_id,
                    player_name,
                    linked_app_user_id
                from player
                where app_user_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(appUserId)
                .query(new PlayerMapper())
                .list();
    }

    @Override
    public Player add(Player player) {

        final String sql = """
                insert into player (
                    app_user_id,
                    player_name,
                    linked_app_user_id
                )
                values (?, ?, ?);
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param(player.getAppUserId())
                .param(player.getPlayerName())
                .param(player.getLinkedAppUserId())
                .update(keyHolder);

        player.setPlayerId(keyHolder.getKey().intValue());

        return player;
    }

    @Override
    public boolean update(Player player) {

        final String sql = """
                update player set
                    app_user_id = ?,
                    player_name = ?,
                    linked_app_user_id = ?
                where player_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(player.getAppUserId())
                .param(player.getPlayerName())
                .param(player.getLinkedAppUserId())
                .param(player.getPlayerId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int playerId) {

        final String sql = """
                delete from player
                where player_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(playerId)
                .update() > 0;
    }
}