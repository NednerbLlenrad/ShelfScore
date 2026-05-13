package learn.scoreshelf.data;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.models.GameSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameSessionRepositoryTests {
    private final TestHelper helper = new TestHelper();
    
    @Autowired
    GameSessionRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    //Find
    @Test
    void shouldFindAll() {
        List<GameSession> sessions = repository.findAll();

        assertNotNull(sessions);
        assertEquals(2, sessions.size());
    }

    @Test
    void shouldFindById() {
        GameSession session = repository.findById(1);

        assertNotNull(session);
        assertEquals(1, session.getGameSessionId());
        assertEquals(1, session.getGameId());
        assertEquals(1, session.getAppUserId());
        assertNotNull(session.getPlayedAt());
    }

    @Test
    void shouldNotFindMissingGameSession() {
        GameSession session = repository.findById(999);

        assertNull(session);
    }

    @Test
    void shouldFindByGameId() {
        List<GameSession> sessions = repository.findByGameId(1);

        assertNotNull(sessions);
        assertEquals(1, sessions.size());
        assertEquals(1, sessions.get(0).getGameId());
    }

    @Test
    void shouldFindByAppUserId() {
        List<GameSession> sessions = repository.findByAppUserId(1);

        assertNotNull(sessions);
        assertEquals(2, sessions.size());
    }

    //Add
    @Test
    void shouldAddGameSession() {
        GameSession session = helper.makeGameSession();

        GameSession actual = repository.add(session);

        assertNotNull(actual);
        assertTrue(actual.getGameSessionId() > 0);

        GameSession saved = repository.findById(actual.getGameSessionId());

        assertEquals(2, saved.getGameId());
        assertEquals(1, saved.getAppUserId());
        assertNotNull(saved.getPlayedAt());
    }

    //Update
    @Test
    void shouldUpdateGameSession() {
        GameSession session = repository.findById(1);
        session.setGameId(2);
        session.setPlayedAt(LocalDateTime.of(2026, 5, 11, 12, 0));

        assertTrue(repository.update(session));

        GameSession actual = repository.findById(1);

        assertEquals(2, actual.getGameId());
        assertEquals(LocalDateTime.of(2026, 5, 11, 12, 0), actual.getPlayedAt());
    }

    @Test
    void shouldNotUpdateMissingGameSession() {
        GameSession session = helper.makeGameSession();
        session.setGameSessionId(999);

        assertFalse(repository.update(session));
    }

    //Delete
    @Test
    void shouldDeleteGameSession() {
        GameSession session = helper.makeGameSession();
        GameSession added = repository.add(session);

        assertTrue(repository.deleteById(added.getGameSessionId()));
        assertNull(repository.findById(added.getGameSessionId()));
    }

    @Test
    void shouldNotDeleteMissingGameSession() {
        assertFalse(repository.deleteById(999));
    }
    
}