package learn.scoreshelf.domain;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.data.GameSessionRepository;
import learn.scoreshelf.models.GameSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GameSessionTest {

    private final TestHelper helper = new TestHelper();

    @Autowired
    GameSessionService service;

    @MockitoBean
    GameSessionRepository repository;

    //Find
    @Test
    void shouldFindAll() {
        List<GameSession> expected = List.of(helper.makeGameSession());

        when(repository.findAll()).thenReturn(expected);

        List<GameSession> actual = service.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        GameSession expected = helper.makeGameSession();
        expected.setGameSessionId(1);

        when(repository.findById(1)).thenReturn(expected);

        GameSession actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindByGameId() {
        List<GameSession> expected = List.of(helper.makeGameSession());

        when(repository.findByGameId(1)).thenReturn(expected);

        List<GameSession> actual = service.findByGameId(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindByAppUserId() {
        List<GameSession> expected = List.of(helper.makeGameSession());

        when(repository.findByAppUserId(1)).thenReturn(expected);

        List<GameSession> actual = service.findByAppUserId(1);

        assertEquals(expected, actual);
    }

    //Add
    @Test
    void shouldAddGameSession() {
        GameSession arg = helper.makeGameSession();

        GameSession expected = helper.makeGameSession();
        expected.setGameSessionId(3);

        when(repository.add(arg)).thenReturn(expected);

        Result<GameSession> result = service.add(arg);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddNullGameSession() {
        Result<GameSession> result = service.add(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenIdIsSet() {
        GameSession session = helper.makeGameSession();
        session.setGameSessionId(1);

        Result<GameSession> result = service.add(session);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(session);
    }

    @Test
    void shouldNotAddWithoutGameId() {
        GameSession session = helper.makeGameSession();
        session.setGameId(0);

        Result<GameSession> result = service.add(session);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(session);
    }

    @Test
    void shouldNotAddWithoutAppUserId() {
        GameSession session = helper.makeGameSession();
        session.setAppUserId(0);

        Result<GameSession> result = service.add(session);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(session);
    }

    @Test
    void shouldNotAddWithoutPlayedAt() {
        GameSession session = helper.makeGameSession();
        session.setPlayedAt(null);

        Result<GameSession> result = service.add(session);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(session);
    }

    //Update
    @Test
    void shouldUpdateGameSession() {
        GameSession session = helper.makeGameSession();
        session.setGameSessionId(1);

        when(repository.update(session)).thenReturn(true);

        Result<GameSession> result = service.update(session);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateNullGameSession() {
        Result<GameSession> result = service.update(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        GameSession session = helper.makeGameSession();

        Result<GameSession> result = service.update(session);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).update(session);
    }

    @Test
    void shouldNotUpdateMissingGameSession() {
        GameSession session = helper.makeGameSession();
        session.setGameSessionId(999);

        when(repository.update(session)).thenReturn(false);

        Result<GameSession> result = service.update(session);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    //Delete
    @Test
    void shouldDeleteGameSession() {
        when(repository.deleteById(1)).thenReturn(true);

        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissingGameSession() {
        when(repository.deleteById(999)).thenReturn(false);

        assertFalse(service.deleteById(999));
    }
}