package learn.scoreshelf.domain;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.data.ScoreEntryRepository;
import learn.scoreshelf.models.ScoreEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScoreEntryServiceTests {

    private final TestHelper helper = new TestHelper();

    @Autowired
    ScoreEntryService service;

    @MockitoBean
    ScoreEntryRepository repository;

    //Find
    @Test
    void shouldFindAll() {
        List<ScoreEntry> expected = List.of(helper.makeScoreEntry());

        when(repository.findAll()).thenReturn(expected);

        List<ScoreEntry> actual = service.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        ScoreEntry expected = helper.makeScoreEntry();
        expected.setScoreEntryId(1);

        when(repository.findById(1)).thenReturn(expected);

        ScoreEntry actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindByGameSessionPlayerId() {
        List<ScoreEntry> expected = List.of(helper.makeScoreEntry());

        when(repository.findByGameSessionPlayerId(1)).thenReturn(expected);

        List<ScoreEntry> actual = service.findByGameSessionPlayerId(1);

        assertEquals(expected, actual);
    }

    //Add
    @Test
    void shouldAddScoreEntry() {
        ScoreEntry arg = helper.makeScoreEntry();

        ScoreEntry expected = helper.makeScoreEntry();
        expected.setScoreEntryId(5);

        when(repository.add(arg)).thenReturn(expected);

        Result<ScoreEntry> result = service.add(arg);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddNullScoreEntry() {
        Result<ScoreEntry> result = service.add(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenIdIsSet() {
        ScoreEntry entry = helper.makeScoreEntry();
        entry.setScoreEntryId(1);

        Result<ScoreEntry> result = service.add(entry);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(entry);
    }

    @Test
    void shouldNotAddWithoutGameSessionPlayerId() {
        ScoreEntry entry = helper.makeScoreEntry();
        entry.setGameSessionPlayerId(0);

        Result<ScoreEntry> result = service.add(entry);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(entry);
    }

    @Test
    void shouldNotAddWithoutScoreSheetRowId() {
        ScoreEntry entry = helper.makeScoreEntry();
        entry.setScoreSheetRowId(0);

        Result<ScoreEntry> result = service.add(entry);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(entry);
    }

    @Test
    void shouldNotAddNegativeValue() {
        ScoreEntry entry = helper.makeScoreEntry();
        entry.setValue(-1);

        Result<ScoreEntry> result = service.add(entry);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(entry);
    }

    //Update
    @Test
    void shouldUpdateScoreEntry() {
        ScoreEntry entry = helper.makeScoreEntry();
        entry.setScoreEntryId(1);

        when(repository.update(entry)).thenReturn(true);

        Result<ScoreEntry> result = service.update(entry);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateNullScoreEntry() {
        Result<ScoreEntry> result = service.update(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        ScoreEntry entry = helper.makeScoreEntry();

        Result<ScoreEntry> result = service.update(entry);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).update(entry);
    }

    @Test
    void shouldNotUpdateMissingScoreEntry() {
        ScoreEntry entry = helper.makeScoreEntry();
        entry.setScoreEntryId(999);

        when(repository.update(entry)).thenReturn(false);

        Result<ScoreEntry> result = service.update(entry);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    //Delete
    @Test
    void shouldDeleteScoreEntry() {
        when(repository.deleteById(1)).thenReturn(true);

        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissingScoreEntry() {
        when(repository.deleteById(999)).thenReturn(false);

        assertFalse(service.deleteById(999));
    }
}