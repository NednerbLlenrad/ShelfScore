package learn.scoreshelf.data;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.models.RowType;
import learn.scoreshelf.models.ScoreSheetRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoreSheetRowRepositoryTests {

    @Autowired
    ScoreSheetRowRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    //Find
    @Test
    void shouldFindAll() {
        List<ScoreSheetRow> rows = repository.findAll();

        assertNotNull(rows);
        assertTrue(rows.size() >= 6);
    }

    @Test
    void shouldFindById() {
        ScoreSheetRow row = repository.findById(1);

        assertNotNull(row);
        assertEquals(1, row.getScoreSheetRowId());
        assertEquals(1, row.getScoreSheetId());
        assertEquals("Birds", row.getRowName());
        assertEquals(1, row.getDisplayOrder());
        assertEquals(RowType.INPUT, row.getRowType());
        assertNull(row.getExpression());
    }

    @Test
    void shouldNotFindMissingScoreSheetRow() {
        ScoreSheetRow row = repository.findById(999);

        assertNull(row);
    }

    @Test
    void shouldFindByScoreSheetId() {
        List<ScoreSheetRow> rows = repository.findByScoreSheetId(1);

        assertNotNull(rows);
        assertEquals(3, rows.size());
        assertEquals("Birds", rows.get(0).getRowName());
        assertEquals("Bonus Cards", rows.get(1).getRowName());
        assertEquals("Total", rows.get(2).getRowName());
    }

    //Add
    @Test
    void shouldAddScoreSheetRow() {
        ScoreSheetRow row = makeScoreSheetRow();

        ScoreSheetRow actual = repository.add(row);

        assertNotNull(actual);
        assertTrue(actual.getScoreSheetRowId() > 0);

        ScoreSheetRow saved = repository.findById(actual.getScoreSheetRowId());

        assertEquals(2, saved.getScoreSheetId());
        assertEquals("Cities", saved.getRowName());
        assertEquals(4, saved.getDisplayOrder());
        assertEquals(RowType.INPUT, saved.getRowType());
        assertNull(saved.getExpression());
    }

    //Update
    @Test
    void shouldUpdateScoreSheetRow() {
        ScoreSheetRow row = repository.findById(1);
        row.setRowName("Bird Cards");
        row.setDisplayOrder(2);
        row.setRowType(RowType.TOTAL);
        row.setExpression("birds + bonus");

        assertTrue(repository.update(row));

        ScoreSheetRow actual = repository.findById(1);

        assertEquals("Bird Cards", actual.getRowName());
        assertEquals(2, actual.getDisplayOrder());
        assertEquals(RowType.TOTAL, actual.getRowType());
        assertEquals("birds + bonus", actual.getExpression());
    }

    @Test
    void shouldNotUpdateMissingScoreSheetRow() {
        ScoreSheetRow row = makeScoreSheetRow();
        row.setScoreSheetRowId(999);

        assertFalse(repository.update(row));
    }

    //Delete
    @Test
    void shouldDeleteScoreSheetRow() {
        assertTrue(repository.deleteById(6));
        assertNull(repository.findById(6));
    }

    @Test
    void shouldNotDeleteMissingScoreSheetRow() {
        assertFalse(repository.deleteById(999));
    }


    //Helpers
    private ScoreSheetRow makeScoreSheetRow() {
        ScoreSheetRow row = new ScoreSheetRow();
        row.setScoreSheetId(2);
        row.setRowName("Cities");
        row.setDisplayOrder(4);
        row.setRowType(RowType.INPUT);
        row.setExpression(null);

        return row;
    }
}