package learn.scoreshelf.models;

import jakarta.validation.constraints.Min;

public class ScoreEntry {

    private int scoreEntryId;

    @Min(value = 1, message = "Game session player ID is required.")
    private int gameSessionPlayerId;

    @Min(value = 1, message = "Score sheet row ID is required.")
    private int scoreSheetRowId;

    @Min(value = 0, message = "Score value cannot be negative.")
    private int value;

    public int getScoreEntryId() {
        return scoreEntryId;
    }

    public void setScoreEntryId(int scoreEntryId) {
        this.scoreEntryId = scoreEntryId;
    }

    public int getGameSessionPlayerId() {
        return gameSessionPlayerId;
    }

    public void setGameSessionPlayerId(int gameSessionPlayerId) {
        this.gameSessionPlayerId = gameSessionPlayerId;
    }

    public int getScoreSheetRowId() {
        return scoreSheetRowId;
    }

    public void setScoreSheetRowId(int scoreSheetRowId) {
        this.scoreSheetRowId = scoreSheetRowId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}