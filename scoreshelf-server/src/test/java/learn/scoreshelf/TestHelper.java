package learn.scoreshelf;

import learn.scoreshelf.models.*;

import java.time.LocalDateTime;

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

    public ScoreSheetRow makeScoreSheetRow() {
        ScoreSheetRow row = new ScoreSheetRow();
        row.setScoreSheetId(1);
        row.setRowName("Birds");
        row.setDisplayOrder(1);
        row.setRowType(RowType.INPUT);
        row.setExpression(null);

        return row;
    }

    public GameSession makeGameSession() {
        GameSession session = new GameSession();
        session.setGameId(2);
        session.setAppUserId(1);
        session.setPlayedAt(LocalDateTime.of(2026, 5, 11, 10, 30));

        return session;
    }
}
