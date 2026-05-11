package learn.scoreshelf.data;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.models.ScoreSheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoreSheetRepositoryTests {
    private final TestHelper helper = new TestHelper();

    @Autowired
    ScoreSheetRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    //Find
    @Test
    void shouldFindAll() {
        List<ScoreSheet> scoreSheets = repository.findAll();

        assertNotNull(scoreSheets);
        assertEquals(3, scoreSheets.size());
    }

    @Test
    void shouldFindById() {
        ScoreSheet scoreSheet = repository.findById(1);

        assertNotNull(scoreSheet);
        assertEquals(1, scoreSheet.getScoreSheetId());
        assertEquals(1, scoreSheet.getGameId());
        assertEquals("Default Wingspan Score Sheet", scoreSheet.getScoreSheetName());
    }

    @Test
    void shouldNotFindMissingScoreSheet() {
        ScoreSheet scoreSheet = repository.findById(999);

        assertNull(scoreSheet);
    }

    //Add
    @Test
    void shouldAddScoreSheet() {
        ScoreSheet scoreSheet = helper.makeScoreSheet();

        ScoreSheet actual = repository.add(scoreSheet);

        assertNotNull(actual);
        assertTrue(actual.getScoreSheetId() > 0);

        ScoreSheet saved = repository.findById(actual.getScoreSheetId());

        assertEquals(2, saved.getGameId());
        assertEquals("Default Catan Score Sheet", saved.getScoreSheetName());
    }

    //Update
    @Test
    void shouldUpdateScoreSheet() {
        ScoreSheet scoreSheet = repository.findById(1);
        scoreSheet.setScoreSheetName("Updated Wingspan Score Sheet");

        assertTrue(repository.update(scoreSheet));

        ScoreSheet actual = repository.findById(1);

        assertEquals("Updated Wingspan Score Sheet", actual.getScoreSheetName());
    }

    @Test
    void shouldNotUpdateMissingScoreSheet() {
        ScoreSheet scoreSheet = helper.makeScoreSheet();
        scoreSheet.setScoreSheetId(999);

        assertFalse(repository.update(scoreSheet));
    }

    //Delete
    @Test
    void shouldDeleteScoreSheet() {
        ScoreSheet scoreSheet = helper.makeScoreSheet();
        ScoreSheet added = repository.add(scoreSheet);

        assertTrue(repository.deleteById(added.getScoreSheetId()));
        assertNull(repository.findById(added.getScoreSheetId()));
    }

    @Test
    void shouldNotDeleteMissingScoreSheet() {
        assertFalse(repository.deleteById(999));
    }

}