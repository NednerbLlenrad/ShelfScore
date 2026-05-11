package learn.scoreshelf.data;

import learn.scoreshelf.models.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameRepositoryTests {

    @Autowired
    GameRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    //Find
    @Test
    void shouldFindAll() {
        List<Game> games = repository.findAll();

        assertNotNull(games);
        assertEquals(3, games.size());
    }

    @Test
    void shouldFindWingspan() {
        Game game = repository.findById(1);

        assertNotNull(game);
        assertEquals(1, game.getGameId());
        assertEquals("Wingspan", game.getGameName());
        assertEquals("Strategy", game.getCategory());
        assertEquals(1, game.getMinPlayers());
        assertEquals(5, game.getMaxPlayers());
        assertFalse(game.isPrivate());
        assertEquals(1, game.getAppUserId());
    }

    @Test
    void shouldNotFindMissingGame() {
        Game game = repository.findById(999);

        assertNull(game);
    }

    //Add
    @Test
    void shouldAddGame() {
        Game game = makeGame();

        Game actual = repository.add(game);

        assertNotNull(actual);
        assertTrue(actual.getGameId() > 0);

        Game saved = repository.findById(actual.getGameId());

        assertEquals("Terraforming Mars", saved.getGameName());
        assertEquals("Strategy", saved.getCategory());
        assertEquals(1, saved.getMinPlayers());
        assertEquals(5, saved.getMaxPlayers());
        assertFalse(saved.isPrivate());
        assertEquals(1, saved.getAppUserId());
    }

    //Update
    @Test
    void shouldUpdateGame() {
        Game game = repository.findById(1);
        game.setGameName("Wingspan Updated");
        game.setCategory("Engine Builder");
        game.setMinPlayers(2);
        game.setMaxPlayers(4);
        game.setPrivate(true);

        assertTrue(repository.update(game));

        Game actual = repository.findById(1);

        assertEquals("Wingspan Updated", actual.getGameName());
        assertEquals("Engine Builder", actual.getCategory());
        assertEquals(2, actual.getMinPlayers());
        assertEquals(4, actual.getMaxPlayers());
        assertTrue(actual.isPrivate());
    }

    @Test
    void shouldNotUpdateMissingGame() {
        Game game = makeGame();
        game.setGameId(999);

        assertFalse(repository.update(game));
    }

    //Delete
    @Test
    void shouldDeleteGame() {
        assertTrue(repository.deleteById(3));
        assertNull(repository.findById(3));
    }

    @Test
    void shouldNotDeleteMissingGame() {
        assertFalse(repository.deleteById(999));
    }


    //Helpers
    private Game makeGame() {
        Game game = new Game();
        game.setGameName("Terraforming Mars");
        game.setCategory("Strategy");
        game.setMinPlayers(1);
        game.setMaxPlayers(5);
        game.setPrivate(false);
        game.setAppUserId(1);

        return game;
    }
}