package learn.scoreshelf.data;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.models.ScoreEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoreEntryRepositoryTests {

    private final TestHelper helper = new TestHelper();

    @Autowired
    ScoreEntryRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    //Find
    @Test
    void shouldFindAll() {
        List<ScoreEntry> entries = repository.findAll();

        assertNotNull(entries);
    }

    @Test
    void shouldFindById() {
        ScoreEntry entry = repository.findById(1);

        assertNotNull(entry);
        assertEquals(1, entry.getScoreEntryId());
    }

    @Test
    void shouldNotFindMissingScoreEntry() {
        ScoreEntry entry = repository.findById(999);

        assertNull(entry);
    }

    @Test
    void shouldFindByGameSessionPlayerId() {
        List<ScoreEntry> entries = repository.findByGameSessionPlayerId(1);

        assertNotNull(entries);
    }

    //Add
    @Test
    void shouldAddScoreEntry() {
        ScoreEntry entry = helper.makeScoreEntry();

        ScoreEntry actual = repository.add(entry);

        assertNotNull(actual);
        assertTrue(actual.getScoreEntryId() > 0);

        ScoreEntry saved = repository.findById(actual.getScoreEntryId());

        assertEquals(entry.getGameSessionPlayerId(), saved.getGameSessionPlayerId());
        assertEquals(entry.getScoreSheetRowId(), saved.getScoreSheetRowId());
        assertEquals(entry.getValue(), saved.getValue());
    }

    //Update
    @Test
    void shouldUpdateScoreEntry() {
        ScoreEntry entry = repository.findById(1);

        entry.setValue(99);

        assertTrue(repository.update(entry));

        ScoreEntry updated = repository.findById(1);

        assertEquals(99, updated.getValue());
    }

    @Test
    void shouldNotUpdateMissingScoreEntry() {
        ScoreEntry entry = helper.makeScoreEntry();
        entry.setScoreEntryId(999);

        assertFalse(repository.update(entry));
    }

    //Delete
    @Test
    void shouldDeleteScoreEntry() {
        ScoreEntry entry = helper.makeScoreEntry();

        ScoreEntry added = repository.add(entry);

        assertTrue(repository.deleteById(added.getScoreEntryId()));
        assertNull(repository.findById(added.getScoreEntryId()));
    }

    @Test
    void shouldNotDeleteMissingScoreEntry() {
        assertFalse(repository.deleteById(999));
    }
}