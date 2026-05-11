package learn.scoreshelf.domain;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.data.GameSessionPlayerRepository;
import learn.scoreshelf.models.GameSessionPlayer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GameSessionPlayerServiceTests {

    private final TestHelper helper = new TestHelper();

    @Autowired
    GameSessionPlayerService service;

    @MockitoBean
    GameSessionPlayerRepository repository;

    //Find
    @Test
    void shouldFindAll() {
        List<GameSessionPlayer> expected = List.of(helper.makeGameSessionPlayer());

        when(repository.findAll()).thenReturn(expected);

        List<GameSessionPlayer> actual = service.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        GameSessionPlayer expected = helper.makeGameSessionPlayer();
        expected.setGameSessionPlayerId(1);

        when(repository.findById(1)).thenReturn(expected);

        GameSessionPlayer actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindByGameSessionId() {
        List<GameSessionPlayer> expected = List.of(helper.makeGameSessionPlayer());

        when(repository.findByGameSessionId(1)).thenReturn(expected);

        List<GameSessionPlayer> actual = service.findByGameSessionId(1);

        assertEquals(expected, actual);
    }

    //Add
    @Test
    void shouldAddGameSessionPlayer() {
        GameSessionPlayer arg = helper.makeGameSessionPlayer();

        GameSessionPlayer expected = helper.makeGameSessionPlayer();
        expected.setGameSessionPlayerId(5);

        when(repository.add(arg)).thenReturn(expected);

        Result<GameSessionPlayer> result = service.add(arg);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddNullGameSessionPlayer() {
        Result<GameSessionPlayer> result = service.add(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenIdIsSet() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();
        player.setGameSessionPlayerId(1);

        Result<GameSessionPlayer> result = service.add(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(player);
    }

    @Test
    void shouldNotAddWithoutGameSessionId() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();
        player.setGameSessionId(0);

        Result<GameSessionPlayer> result = service.add(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(player);
    }

    @Test
    void shouldNotAddWithoutPlayerName() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();
        player.setPlayerName(null);

        Result<GameSessionPlayer> result = service.add(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(player);
    }

    @Test
    void shouldNotAddNegativeScore() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();
        player.setTotalScore(-1);

        Result<GameSessionPlayer> result = service.add(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(player);
    }

    @Test
    void shouldNotAddWithoutWinnerStatus() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();
        player.setIsWinner(null);

        Result<GameSessionPlayer> result = service.add(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(player);
    }

    //Update
    @Test
    void shouldUpdateGameSessionPlayer() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();
        player.setGameSessionPlayerId(1);

        when(repository.update(player)).thenReturn(true);

        Result<GameSessionPlayer> result = service.update(player);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateNullGameSessionPlayer() {
        Result<GameSessionPlayer> result = service.update(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();

        Result<GameSessionPlayer> result = service.update(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).update(player);
    }

    @Test
    void shouldNotUpdateMissingGameSessionPlayer() {
        GameSessionPlayer player = helper.makeGameSessionPlayer();
        player.setGameSessionPlayerId(999);

        when(repository.update(player)).thenReturn(false);

        Result<GameSessionPlayer> result = service.update(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    //Delete
    @Test
    void shouldDeleteGameSessionPlayer() {
        when(repository.deleteById(1)).thenReturn(true);

        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissingGameSessionPlayer() {
        when(repository.deleteById(999)).thenReturn(false);

        assertFalse(service.deleteById(999));
    }
}