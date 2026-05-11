package learn.scoreshelf;

import learn.scoreshelf.models.Game;
import learn.scoreshelf.models.ScoreSheet;

public class TestHelper {

    public Game makeGame() {
        Game game = new Game();
        game.setGameName("Terraforming Mars");
        game.setCategory("Strategy");
        game.setMinPlayers(1);
        game.setMaxPlayers(5);
        game.setPrivate(false);
        game.setAppUserId(1);

        return game;
    }

    public ScoreSheet makeScoreSheet() {
        ScoreSheet scoreSheet = new ScoreSheet();
        scoreSheet.setGameId(2);
        scoreSheet.setScoreSheetName("Default Catan Score Sheet");

        return scoreSheet;
    }
}
