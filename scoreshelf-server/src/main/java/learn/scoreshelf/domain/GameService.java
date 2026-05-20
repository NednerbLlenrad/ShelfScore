package learn.scoreshelf.domain;

import learn.scoreshelf.data.GameRepository;
import learn.scoreshelf.data.ScoreSheetRepository;
import learn.scoreshelf.data.ScoreSheetRowRepository;
import learn.scoreshelf.models.Game;
import learn.scoreshelf.models.ScoreSheet;
import learn.scoreshelf.models.ScoreSheetRow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository repository;
    private final ScoreSheetRepository scoreSheetRepository;
    private final ScoreSheetRowRepository scoreSheetRowRepository;

    public GameService(
            GameRepository repository,
            ScoreSheetRepository scoreSheetRepository,
            ScoreSheetRowRepository scoreSheetRowRepository
    ) {
        this.repository = repository;
        this.scoreSheetRepository = scoreSheetRepository;
        this.scoreSheetRowRepository = scoreSheetRowRepository;
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

    public List<Game> findByAppUserId(int appUserId) {
        return repository.findByAppUserId(appUserId);
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

    public Result<Game> copyToLibrary(int originalGameId, int appUserId) {
        Result<Game> result = new Result<>();

        Game original = repository.findById(originalGameId);

        if (original == null) {
            result.addMessage("Game not found.", ResultType.NOT_FOUND);
            return result;
        }

        Game copy = new Game();

        copy.setGameName(original.getGameName());
        copy.setImageUrl(original.getImageUrl());
        copy.setCategory(original.getCategory());
        copy.setMinPlayers(original.getMinPlayers());
        copy.setMaxPlayers(original.getMaxPlayers());
        copy.setPrivate(true);
        copy.setAppUserId(appUserId);

        Game savedGame = repository.add(copy);

        List<ScoreSheet> originalSheets =
                scoreSheetRepository.findByGameId(originalGameId);

        for (ScoreSheet originalSheet : originalSheets) {
            ScoreSheet sheetCopy = new ScoreSheet();

            sheetCopy.setGameId(savedGame.getGameId());
            sheetCopy.setScoreSheetName(originalSheet.getScoreSheetName());

            ScoreSheet savedSheet = scoreSheetRepository.add(sheetCopy);

            List<ScoreSheetRow> originalRows =
                    scoreSheetRowRepository.findByScoreSheetId(
                            originalSheet.getScoreSheetId()
                    );

            for (ScoreSheetRow originalRow : originalRows) {
                ScoreSheetRow rowCopy = new ScoreSheetRow();

                rowCopy.setScoreSheetId(savedSheet.getScoreSheetId());
                rowCopy.setRowName(originalRow.getRowName());
                rowCopy.setDisplayOrder(originalRow.getDisplayOrder());
                rowCopy.setRowType(originalRow.getRowType());
                rowCopy.setExpression(originalRow.getExpression());

                scoreSheetRowRepository.add(rowCopy);
            }
        }

        result.setPayload(savedGame);
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
}