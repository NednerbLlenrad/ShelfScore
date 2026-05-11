package learn.scoreshelf.domain;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.data.ScoreSheetRepository;
import learn.scoreshelf.models.ScoreSheet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScoreSheetServiceTests {

    private final TestHelper helper = new TestHelper();

    @Autowired
    ScoreSheetService service;

    @MockitoBean
    ScoreSheetRepository repository;

    //Find
    @Test
    void shouldFindAll() {
        List<ScoreSheet> expected = List.of(helper.makeScoreSheet());

        when(repository.findAll()).thenReturn(expected);

        List<ScoreSheet> actual = service.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        ScoreSheet expected = helper.makeScoreSheet();
        expected.setScoreSheetId(1);

        when(repository.findById(1)).thenReturn(expected);

        ScoreSheet actual = service.findById(1);

        assertEquals(expected, actual);
    }

    //Add
    @Test
    void shouldAddScoreSheet() {
        ScoreSheet arg = helper.makeScoreSheet();

        ScoreSheet expected = helper.makeScoreSheet();
        expected.setScoreSheetId(4);

        when(repository.add(arg)).thenReturn(expected);

        Result<ScoreSheet> result = service.add(arg);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddNullScoreSheet() {
        Result<ScoreSheet> result = service.add(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenIdIsSet() {
        ScoreSheet scoreSheet = helper.makeScoreSheet();
        scoreSheet.setScoreSheetId(1);

        Result<ScoreSheet> result = service.add(scoreSheet);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(scoreSheet);
    }

    @Test
    void shouldNotAddWithoutGameId() {
        ScoreSheet scoreSheet = helper.makeScoreSheet();
        scoreSheet.setGameId(0);

        Result<ScoreSheet> result = service.add(scoreSheet);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(scoreSheet);
    }

    @Test
    void shouldNotAddWithoutName() {
        ScoreSheet scoreSheet = helper.makeScoreSheet();
        scoreSheet.setScoreSheetName(null);

        Result<ScoreSheet> result = service.add(scoreSheet);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(scoreSheet);
    }

    //Update
    @Test
    void shouldUpdateScoreSheet() {
        ScoreSheet scoreSheet = helper.makeScoreSheet();
        scoreSheet.setScoreSheetId(1);

        when(repository.update(scoreSheet)).thenReturn(true);

        Result<ScoreSheet> result = service.update(scoreSheet);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateNullScoreSheet() {
        Result<ScoreSheet> result = service.update(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        ScoreSheet scoreSheet = helper.makeScoreSheet();

        Result<ScoreSheet> result = service.update(scoreSheet);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).update(scoreSheet);
    }

    @Test
    void shouldNotUpdateMissingScoreSheet() {
        ScoreSheet scoreSheet = helper.makeScoreSheet();
        scoreSheet.setScoreSheetId(999);

        when(repository.update(scoreSheet)).thenReturn(false);

        Result<ScoreSheet> result = service.update(scoreSheet);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    //Delete
    @Test
    void shouldDeleteScoreSheet() {
        when(repository.deleteById(1)).thenReturn(true);

        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissingScoreSheet() {
        when(repository.deleteById(999)).thenReturn(false);

        assertFalse(service.deleteById(999));
    }
}