package learn.scoreshelf.domain;

import learn.scoreshelf.data.GameSessionPlayerRepository;
import learn.scoreshelf.models.GameSessionPlayer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameSessionPlayerService {

    private final GameSessionPlayerRepository repository;

    public GameSessionPlayerService(GameSessionPlayerRepository repository) {
        this.repository = repository;
    }

    public List<GameSessionPlayer> findAll() {
        return repository.findAll();
    }

    public GameSessionPlayer findById(int gameSessionPlayerId) {
        return repository.findById(gameSessionPlayerId);
    }

    public List<GameSessionPlayer> findByGameSessionId(int gameSessionId) {
        return repository.findByGameSessionId(gameSessionId);
    }

    public Result<GameSessionPlayer> add(GameSessionPlayer player) {

        Result<GameSessionPlayer> result = validate(player);

        if (!result.isSuccess()) {
            return result;
        }

        if (player.getGameSessionPlayerId() != 0) {
            result.addMessage("Game session player ID cannot be set for add operation.", ResultType.INVALID);
            return result;
        }

        player = repository.add(player);
        result.setPayload(player);

        return result;
    }

    public Result<GameSessionPlayer> update(GameSessionPlayer player) {

        Result<GameSessionPlayer> result = validate(player);

        if (!result.isSuccess()) {
            return result;
        }

        if (player.getGameSessionPlayerId() <= 0) {
            result.addMessage("Game session player ID is required for update.", ResultType.INVALID);
            return result;
        }

        if (!repository.update(player)) {
            result.addMessage("Game session player not found.", ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int gameSessionPlayerId) {
        return repository.deleteById(gameSessionPlayerId);
    }

    //Helpers
    private Result<GameSessionPlayer> validate(GameSessionPlayer player) {

        Result<GameSessionPlayer> result = new Result<>();

        if (player == null) {
            result.addMessage("Game session player cannot be null.", ResultType.INVALID);
            return result;
        }

        if (player.getGameSessionId() <= 0) {
            result.addMessage("Game session ID is required.", ResultType.INVALID);
        }

        if (player.getPlayerName() == null || player.getPlayerName().isBlank()) {
            result.addMessage("Player name is required.", ResultType.INVALID);
        }

        if (player.getPlayerName() != null && player.getPlayerName().length() > 50) {
            result.addMessage("Player name must be 50 characters or less.", ResultType.INVALID);
        }

        if (player.getTotalScore() < 0) {
            result.addMessage("Total score cannot be negative.", ResultType.INVALID);
        }

        if (player.getIsWinner() == null) {
            result.addMessage("Winner status is required.", ResultType.INVALID);
        }

        return result;
    }
}