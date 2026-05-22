package learn.scoreshelf.data;

import learn.scoreshelf.data.mappers.GameMapper;
import learn.scoreshelf.models.Game;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class GameRepositoryJdbcClient implements GameRepository{

    private final JdbcClient jdbcClient;

    public GameRepositoryJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Game> findAll() {

        final String sql = """
            select
                game_id,
                game_name,
                image_url,
                category,
                min_players,
                max_players,
                is_private,
                app_user_id
            from game;
            """;

        return jdbcClient.sql(sql)
                .query(new GameMapper())
                .list();
    }

    @Override
    public Game findById(int gameId) {

        final String sql = """
            select
                game_id,
                game_name,
                image_url,
                category,
                min_players,
                max_players,
                is_private,
                app_user_id
            from game
            where game_id = ?;
            """;

        return jdbcClient.sql(sql)
                .param(gameId)
                .query(new GameMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public Game add(Game game) {

        final String sql = """
            insert into game (
                game_name,
                image_url,
                category,
                min_players,
                max_players,
                is_private,
                app_user_id
            )
            values (?, ?, ?, ?, ?, ?, ?);
            """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param(game.getGameName())
                .param(game.getImageUrl())
                .param(game.getCategory())
                .param(game.getMinPlayers())
                .param(game.getMaxPlayers())
                .param(game.isPrivate())
                .param(game.getAppUserId())
                .update(keyHolder);

        game.setGameId(keyHolder.getKey().intValue());

        return game;
    }

    @Override
    public boolean update(Game game) {

        final String sql = """
            update game set
                game_name = ?,
                image_url = ?,
                category = ?,
                min_players = ?,
                max_players = ?,
                is_private = ?,
                app_user_id = ?
            where game_id = ?;
            """;

        return jdbcClient.sql(sql)
                .param(game.getGameName())
                .param(game.getImageUrl())
                .param(game.getCategory())
                .param(game.getMinPlayers())
                .param(game.getMaxPlayers())
                .param(game.isPrivate())
                .param(game.getAppUserId())
                .param(game.getGameId())
                .update() > 0;
    }

    @Transactional
    @Override
    public boolean deleteById(int gameId) {

<<<<<<< Updated upstream
=======
        jdbcClient.sql("""
        delete se
        from score_entry se
        inner join game_session_player gsp
            on se.game_session_player_id = gsp.game_session_player_id
        inner join game_session gs
            on gsp.game_session_id = gs.game_session_id
        where gs.game_id = ?;
        """)
                .param(gameId)
                .update();

        jdbcClient.sql("""
        delete gsp
        from game_session_player gsp
        inner join game_session gs
            on gsp.game_session_id = gs.game_session_id
        where gs.game_id = ?;
        """)
                .param(gameId)
                .update();

        int deletedSessions = jdbcClient.sql("""
        delete from game_session
        where game_id = ?;
        """)
                .param(gameId)
                .update();

        System.out.println("Deleted sessions: " + deletedSessions);

        jdbcClient.sql("""
            delete from score_sheet_row
            where score_sheet_id in (
                select score_sheet_id
                from score_sheet
                where game_id = ?
            );
            """)
                .param(gameId)
                .update();

        jdbcClient.sql("""
            delete from score_sheet
            where game_id = ?;
            """)
                .param(gameId)
                .update();

        jdbcClient.sql("""
            delete from user_game_library
            where game_id = ?;
            """)
                .param(gameId)
                .update();

>>>>>>> Stashed changes
        final String sql = """
            delete from game
            where game_id = ?;
            """;

        return jdbcClient.sql(sql)
                .param(gameId)
                .update() > 0;
    }

    @Override
    public List<Game> findPublicGames() {

        final String sql = """
            select
                game_id,
                game_name,
                image_url,
                category,
                min_players,
                max_players,
                is_private,
                app_user_id
            from game
            where is_private = false;
            """;

        return jdbcClient.sql(sql)
                .query(new GameMapper())
                .list();
    }

    @Override
    public List<Game> findAccessibleGames(int appUserId) {

        final String sql = """
        select
            game_id,
            game_name,
            image_url,
            category,
            min_players,
            max_players,
            is_private,
            app_user_id
        from game
        where is_private = false
            and app_user_id <> ?;
        """;

        return jdbcClient.sql(sql)
                .param(appUserId)
                .query(new GameMapper())
                .list();
    }

    @Override
    public List<Game> findByAppUserId(int appUserId) {

        final String sql = """
        select
            game_id,
            game_name,
            image_url,
            category,
            min_players,
            max_players,
            is_private,
            app_user_id
        from game
        where app_user_id = ?;
        """;

        return jdbcClient.sql(sql)
                .param(appUserId)
                .query(new GameMapper())
                .list();
    }
}
