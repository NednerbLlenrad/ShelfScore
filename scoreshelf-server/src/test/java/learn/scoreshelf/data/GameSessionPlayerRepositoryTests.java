package learn.scoreshelf.data;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.models.GameSessionPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameSessionPlayerRepositoryTests {

    private final TestHelper helper = new TestHelper();

    @Autowired
    GameSessionPlayerRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    //Find
    @Test
    void shouldFindAll() {
        List<GameSessionPlayer> players = repository.findAll();

        assertNotNull(players);
        assertEquals(4, players.size());
    }

    @Test
    void shouldFindById() {
        GameSessionPlayer player = repository.findById(1);

        assertNotNull(player);
        assertEquals(1, player.getGameSessionPlayerId());
        assertEquals(1, player.getGameSessionId());
        assertEquals("Brenden", player.getPlayerName());
        assertEquals(45, player.getTotalScore());
        assertFalse(player.getIsWinner());
    }

    @Test
    void shouldNotFindMissingGameSessionPlayer() {
        GameSessionPlayer player = repository.findById(999);

        assertNull(player);
    }

    @Test
    void shouldFindByGameSessionId() {
        List<GameSessionPlayer> players = repository.findByGameSessionId(1);

        assertNotNull(players);
        assertEquals(2, players.size());
    }

    //Add
    @Test
    void shouldAddGameSessionPlayer() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();

        GameSessionPlayer actual = repository.add(player);

        assertNotNull(actual);
        assertTrue(actual.getGameSessionPlayerId() > 0);

        GameSessionPlayer saved = repository.findById(actual.getGameSessionPlayerId());

        assertEquals(1, saved.getGameSessionId());
        assertEquals("Jake", saved.getPlayerName());
        assertEquals(31, saved.getTotalScore());
        assertFalse(saved.getIsWinner());
    }

    //Update
    @Test
    void shouldUpdateGameSessionPlayer() {
        GameSessionPlayer player = repository.findById(1);
        player.setPlayerName("Brenden Updated");
        player.setTotalScore(99);
        player.setIsWinner(true);

        assertTrue(repository.update(player));

        GameSessionPlayer actual = repository.findById(1);

        assertEquals("Brenden Updated", actual.getPlayerName());
        assertEquals(99, actual.getTotalScore());
        assertTrue(actual.getIsWinner());
    }

    @Test
    void shouldNotUpdateMissingGameSessionPlayer() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();
        player.setGameSessionPlayerId(999);

        assertFalse(repository.update(player));
    }

    //Delete
    @Test
    void shouldDeleteGameSessionPlayer() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();
        GameSessionPlayer added = repository.add(player);

        assertTrue(repository.deleteById(added.getGameSessionPlayerId()));
        assertNull(repository.findById(added.getGameSessionPlayerId()));
    }

    @Test
    void shouldNotDeleteMissingGameSessionPlayer() {
        assertFalse(repository.deleteById(999));
    }
}