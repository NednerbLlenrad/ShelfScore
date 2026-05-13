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
        player.setPlayerName("bob");
        player.setLinkedAppUserId(null);

        return player;
    }

    public AppUser makeAppUser() {
        AppUser user = new AppUser();

        user.setUsername("bob");
        user.setEmail("bob@test.com");
        user.setPasswordHash("hashed-password");

        return user;
    }

    public GameSession makeGameSession() {

        GameSession session = new GameSession();

        session.setGameId(1);
        session.setAppUserId(1);
        session.setPlayedAt(LocalDateTime.now());

        return session;
    }

    public GameSessionPlayer makeGameSessionPlayer() {

        GameSessionPlayer player = new GameSessionPlayer();

        player.setGameSessionId(1);
        player.setPlayerId(1);
        player.setPlayerName("Test Player");
        player.setTotalScore(31);
        player.setIsWinner(false);

        return player;
    }
}
