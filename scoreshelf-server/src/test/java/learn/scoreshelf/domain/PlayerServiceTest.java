package learn.scoreshelf.domain;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.data.PlayerRepository;
import learn.scoreshelf.models.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlayerServiceTest {

    private final TestHelper helper = new TestHelper();

    @Autowired
    PlayerService service;

    @MockitoBean
    PlayerRepository repository;

    //Find
    @Test
    void shouldFindAll() {
        List<Player> expected = List.of(helper.makePlayer());

        when(repository.findAll()).thenReturn(expected);

        List<Player> actual = service.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        Player expected = helper.makePlayer();
        expected.setPlayerId(1);

        when(repository.findById(1)).thenReturn(expected);

        Player actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindByAppUserId() {
        List<Player> expected = List.of(helper.makePlayer());

        when(repository.findByAppUserId(1)).thenReturn(expected);

        List<Player> actual = service.findByAppUserId(1);

        assertEquals(expected, actual);
    }

    //Add
    @Test
    void shouldAddPlayer() {
        Player arg = helper.makePlayer();

        Player expected = helper.makePlayer();
        expected.setPlayerId(5);

        when(repository.add(arg)).thenReturn(expected);

        Result<Player> result = service.add(arg);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddNullPlayer() {
        Result<Player> result = service.add(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenIdIsSet() {
        Player player = helper.makePlayer();
        player.setPlayerId(1);

        Result<Player> result = service.add(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(player);
    }

    @Test
    void shouldNotAddWithoutAppUserId() {
        Player player = helper.makePlayer();
        player.setAppUserId(0);

        Result<Player> result = service.add(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(player);
    }

    @Test
    void shouldNotAddWithoutPlayerName() {
        Player player = helper.makePlayer();
        player.setPlayerName(null);

        Result<Player> result = service.add(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(player);
    }

    @Test
    void shouldNotAddWhenPlayerNameTooLong() {
        Player player = helper.makePlayer();
        player.setPlayerName("a".repeat(51));

        Result<Player> result = service.add(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(player);
    }

    //Update
    @Test
    void shouldUpdatePlayer() {
        Player player = helper.makePlayer();
        player.setPlayerId(1);

        when(repository.update(player)).thenReturn(true);

        Result<Player> result = service.update(player);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateNullPlayer() {
        Result<Player> result = service.update(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        Player player = helper.makePlayer();

        Result<Player> result = service.update(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).update(player);
    }

    @Test
    void shouldNotUpdateMissingPlayer() {
        Player player = helper.makePlayer();
        player.setPlayerId(999);

        when(repository.update(player)).thenReturn(false);

        Result<Player> result = service.update(player);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    //Delete
    @Test
    void shouldDeletePlayer() {
        when(repository.deleteById(1)).thenReturn(true);

        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissingPlayer() {
        when(repository.deleteById(999)).thenReturn(false);

        assertFalse(service.deleteById(999));
    }
}