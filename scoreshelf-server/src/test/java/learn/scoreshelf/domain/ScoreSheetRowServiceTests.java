package learn.scoreshelf.domain;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.data.ScoreSheetRowRepository;
import learn.scoreshelf.models.RowType;
import learn.scoreshelf.models.ScoreSheetRow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScoreSheetRowServiceTests {
    private final TestHelper helper = new TestHelper();
    
    @Autowired
    ScoreSheetRowService service;

    @MockitoBean
    ScoreSheetRowRepository repository;

    //Find
    @Test
    void shouldFindAll() {
        List<ScoreSheetRow> expected = List.of(helper.makeScoreSheetRow());

        when(repository.findAll()).thenReturn(expected);

        List<ScoreSheetRow> actual = service.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        ScoreSheetRow expected = helper.makeScoreSheetRow();
        expected.setScoreSheetRowId(1);

        when(repository.findById(1)).thenReturn(expected);

        ScoreSheetRow actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindByScoreSheetId() {
        List<ScoreSheetRow> expected = List.of(helper.makeScoreSheetRow());

        when(repository.findByScoreSheetId(1)).thenReturn(expected);

        List<ScoreSheetRow> actual = service.findByScoreSheetId(1);

        assertEquals(expected, actual);
    }

    //Add
    @Test
    void shouldAddScoreSheetRow() {
        ScoreSheetRow arg = helper.makeScoreSheetRow();

        ScoreSheetRow expected = helper.makeScoreSheetRow();
        expected.setScoreSheetRowId(4);

        when(repository.add(arg)).thenReturn(expected);

        Result<ScoreSheetRow> result = service.add(arg);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddNullScoreSheetRow() {
        Result<ScoreSheetRow> result = service.add(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenIdIsSet() {
        ScoreSheetRow row = helper.makeScoreSheetRow();
        row.setScoreSheetRowId(1);

        Result<ScoreSheetRow> result = service.add(row);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(row);
    }

    @Test
    void shouldNotAddWithoutScoreSheetId() {
        ScoreSheetRow row = helper.makeScoreSheetRow();
        row.setScoreSheetId(0);

        Result<ScoreSheetRow> result = service.add(row);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(row);
    }

    @Test
    void shouldNotAddWithoutRowName() {
        ScoreSheetRow row = helper.makeScoreSheetRow();
        row.setRowName(null);

        Result<ScoreSheetRow> result = service.add(row);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(row);
    }

    @Test
    void shouldNotAddWithoutDisplayOrder() {
        ScoreSheetRow row = helper.makeScoreSheetRow();
        row.setDisplayOrder(0);

        Result<ScoreSheetRow> result = service.add(row);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(row);
    }

    @Test
    void shouldNotAddWithoutRowType() {
        ScoreSheetRow row = helper.makeScoreSheetRow();
        row.setRowType(null);

        Result<ScoreSheetRow> result = service.add(row);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(row);
    }

    //Update
    @Test
    void shouldUpdateScoreSheetRow() {
        ScoreSheetRow row = helper.makeScoreSheetRow();
        row.setScoreSheetRowId(1);

        when(repository.update(row)).thenReturn(true);

        Result<ScoreSheetRow> result = service.update(row);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateNullScoreSheetRow() {
        Result<ScoreSheetRow> result = service.update(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        ScoreSheetRow row = helper.makeScoreSheetRow();

        Result<ScoreSheetRow> result = service.update(row);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).update(row);
    }

    @Test
    void shouldNotUpdateMissingScoreSheetRow() {
        ScoreSheetRow row = helper.makeScoreSheetRow();
        row.setScoreSheetRowId(999);

        when(repository.update(row)).thenReturn(false);

        Result<ScoreSheetRow> result = service.update(row);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    //Delete
    @Test
    void shouldDeleteScoreSheetRow() {
        when(repository.deleteById(1)).thenReturn(true);

        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissingScoreSheetRow() {
        when(repository.deleteById(999)).thenReturn(false);

        assertFalse(service.deleteById(999));
    }

}