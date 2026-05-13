package learn.scoreshelf.data;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlayerRepositoryTests {

    private final TestHelper helper = new TestHelper();

    @Autowired
    PlayerRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    //Find
    @Test
    void shouldFindAll() {
        List<Player> players = repository.findAll();

        assertNotNull(players);
        assertEquals(2, players.size());
    }

    @Test
    void shouldFindById() {
        Player player = repository.findById(1);

        assertNotNull(player);
        assertEquals(1, player.getPlayerId());
        assertEquals(1, player.getAppUserId());
        assertEquals("Brenden", player.getPlayerName());
        assertEquals(1, player.getLinkedAppUserId());
    }

    @Test
    void shouldNotFindMissingPlayer() {
        Player player = repository.findById(999);

        assertNull(player);
    }

    @Test
    void shouldFindByAppUserId() {
        List<Player> players = repository.findByAppUserId(1);

        assertNotNull(players);
        assertEquals(2, players.size());
    }

    //Add
    @Test
    void shouldAddPlayer() {
        Player player = helper.makePlayer();

        Player actual = repository.add(player);

        assertNotNull(actual);
        assertTrue(actual.getPlayerId() > 0);

        Player saved = repository.findById(actual.getPlayerId());

        assertEquals(1, saved.getAppUserId());
        assertEquals("Jake", saved.getPlayerName());
        assertNull(saved.getLinkedAppUserId());
    }

    //Update
    @Test
    void shouldUpdatePlayer() {
        Player player = repository.findById(2);
        player.setPlayerName("Sally Updated");
        player.setLinkedAppUserId(2);

        assertTrue(repository.update(player));

        Player actual = repository.findById(2);

        assertEquals("Sally Updated", actual.getPlayerName());
        assertEquals(2, actual.getLinkedAppUserId());
    }

    @Test
    void shouldNotUpdateMissingPlayer() {
        Player player = helper.makePlayer();
        player.setPlayerId(999);

        assertFalse(repository.update(player));
    }

    //Delete
    @Test
    void shouldDeletePlayer() {
        Player player = helper.makePlayer();
        Player added = repository.add(player);

        assertTrue(repository.deleteById(added.getPlayerId()));
        assertNull(repository.findById(added.getPlayerId()));
    }

    @Test
    void shouldNotDeleteMissingPlayer() {
        assertFalse(repository.deleteById(999));
    }
}