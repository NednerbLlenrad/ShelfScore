package learn.scoreshelf.domain;

import learn.scoreshelf.data.GameRepository;
import learn.scoreshelf.models.Game;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository repository;

    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public List<Game> findAll(){
        return repository.findAll();
    }

    public Game findById(int gameId){
        return repository.findById(gameId);
    }

    public List<Game> findPublicGames() {
        return repository.findPublicGames();
    }

    public List<Game> findAccessibleGames(int appUserId) {
        return repository.findAccessibleGames(appUserId);
    }

    public Result<Game> add(Game game) {

        Result<Game> result = validate(game);

        if (!result.isSuccess()) {
            return result;
        }

        if (game.getGameId() != 0) {
            result.addMessage("Game ID cannot be set for add operation.", ResultType.INVALID);
            return result;
        }

        game = repository.add(game);
        result.setPayload(game);

        return result;
    }

    public Result<Game> update(Game game) {

        Result<Game> result = validate(game);

        if (!result.isSuccess()) {
            return result;
        }

        if (game.getGameId() <= 0) {
            result.addMessage("Game ID is required for update.", ResultType.INVALID);
            return result;
        }

        if (!repository.update(game)) {
            result.addMessage("Game not found.", ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int gameId) {
        return repository.deleteById(gameId);
    }

    //Helpers
    private Result<Game> validate(Game game) {

        Result<Game> result = new Result<>();

        if (game == null) {
            result.addMessage("Game cannot be null.", ResultType.INVALID);
            return result;
        }

        if (game.getMinPlayers() < 1) {
            result.addMessage("Minimum players must be at least 1.", ResultType.INVALID);
        }

        if (game.getMaxPlayers() < game.getMinPlayers()) {
            result.addMessage("Maximum players must be greater than or equal to minimum players.", ResultType.INVALID);
        }

        return result;
    }

    public List<Game> findByAppUserId(int appUserId) {
        return repository.findByAppUserId(appUserId);
    }
}

