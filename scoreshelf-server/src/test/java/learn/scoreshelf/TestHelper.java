package learn.scoreshelf;

import learn.scoreshelf.models.*;

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

    public ScoreEntry makeScoreEntry() {
        ScoreEntry entry = new ScoreEntry();
        entry.setGameSessionPlayerId(1);
        entry.setScoreSheetRowId(1);
        entry.setValue(12);

        return entry;
    }

    public Player makePlayer() {
        Player player = new Player();
        player.setAppUserId(1);
        player.setPlayerName("Jake");
        player.setLinkedAppUserId(null);

        return player;
    }
}
