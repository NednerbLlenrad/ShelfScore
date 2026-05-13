package learn.scoreshelf.domain;

import learn.scoreshelf.data.PlayerRepository;
import learn.scoreshelf.models.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public List<Player> findAll() {
        return repository.findAll();
    }

    public Player findById(int playerId) {
        return repository.findById(playerId);
    }

    public List<Player> findByAppUserId(int appUserId) {
        return repository.findByAppUserId(appUserId);
    }

    public Result<Player> add(Player player) {

        Result<Player> result = validate(player);

        if (!result.isSuccess()) {
            return result;
        }

        if (player.getPlayerId() != 0) {
            result.addMessage("Player ID cannot be set for add operation.", ResultType.INVALID);
            return result;
        }

        player = repository.add(player);
        result.setPayload(player);

        return result;
    }

    public Result<Player> update(Player player) {

        Result<Player> result = validate(player);

        if (!result.isSuccess()) {
            return result;
        }

        if (player.getPlayerId() <= 0) {
            result.addMessage("Player ID is required for update.", ResultType.INVALID);
            return result;
        }

        if (!repository.update(player)) {
            result.addMessage("Player not found.", ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int playerId) {
        return repository.deleteById(playerId);
    }

    //Helpers
    private Result<Player> validate(Player player) {

        Result<Player> result = new Result<>();

        if (player == null) {
            result.addMessage("Player cannot be null.", ResultType.INVALID);
            return result;
        }

        if (player.getAppUserId() <= 0) {
            result.addMessage("App user ID is required.", ResultType.INVALID);
        }

        if (player.getPlayerName() == null || player.getPlayerName().isBlank()) {
            result.addMessage("Player name is required.", ResultType.INVALID);
        }

        if (player.getPlayerName() != null && player.getPlayerName().length() > 50) {
            result.addMessage("Player name must be 50 characters or less.", ResultType.INVALID);
        }

        return result;
    }
}