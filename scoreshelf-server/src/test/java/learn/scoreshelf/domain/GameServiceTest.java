package learn.scoreshelf.domain;

import learn.scoreshelf.data.GameRepository;
import learn.scoreshelf.models.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GameServiceTest {

    @Autowired
    GameService service;

    @MockitoBean
    GameRepository repository;

    //Find
    @Test
    void shouldFindAll() {
        List<Game> expected = List.of(makeGame());

        when(repository.findAll()).thenReturn(expected);

        List<Game> actual = service.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        Game expected = makeGame();
        expected.setGameId(1);

        when(repository.findById(1)).thenReturn(expected);

        Game actual = service.findById(1);

        assertEquals(expected, actual);
    }

    //Add
    @Test
    void shouldAddGame() {
        Game arg = makeGame();
        Game expected = makeGame();
        expected.setGameId(4);

        when(repository.add(arg)).thenReturn(expected);

        Result<Game> result = service.add(arg);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddNullGame() {
        Result<Game> result = service.add(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenGameIdIsSet() {
        Game game = makeGame();
        game.setGameId(1);

        Result<Game> result = service.add(game);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(game);
    }

    @Test
    void shouldNotAddWhenMinPlayersLessThanOne() {
        Game game = makeGame();
        game.setMinPlayers(0);

        Result<Game> result = service.add(game);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(game);
    }

    @Test
    void shouldNotAddWhenMaxPlayersLessThanMinPlayers() {
        Game game = makeGame();
        game.setMinPlayers(5);
        game.setMaxPlayers(2);

        Result<Game> result = service.add(game);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(game);
    }

    //Update
    @Test
    void shouldUpdateGame() {
        Game game = makeGame();
        game.setGameId(1);

        when(repository.update(game)).thenReturn(true);

        Result<Game> result = service.update(game);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateNullGame() {
        Result<Game> result = service.update(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWhenGameIdIsMissing() {
        Game game = makeGame();

        Result<Game> result = service.update(game);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).update(game);
    }

    @Test
    void shouldNotUpdateMissingGame() {
        Game game = makeGame();
        game.setGameId(999);

        when(repository.update(game)).thenReturn(false);

        Result<Game> result = service.update(game);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    //Delete
    @Test
    void shouldDeleteGame() {
        when(repository.deleteById(1)).thenReturn(true);

        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissingGame() {
        when(repository.deleteById(999)).thenReturn(false);

        assertFalse(service.deleteById(999));
    }


    //Helpers
    private Game makeGame() {
        Game game = new Game();
        game.setGameName("Terraforming Mars");
        game.setCategory("Strategy");
        game.setMinPlayers(1);
        game.setMaxPlayers(5);
        game.setPrivate(false);
        game.setAppUserId(1);

        return game;
    }
}