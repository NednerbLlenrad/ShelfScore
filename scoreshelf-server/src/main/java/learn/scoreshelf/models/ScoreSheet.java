package learn.scoreshelf.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ScoreSheet {

    private int scoreSheetId;

    private int gameId;

    @NotBlank(message = "Score sheet name is required.")
    @Size(max = 50, message = "Score sheet name must be 50 characters or less.")
    private String scoreSheetName;

    public int getScoreSheetId() {
        return scoreSheetId;
    }

    public void setScoreSheetId(int scoreSheetId) {
        this.scoreSheetId = scoreSheetId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getScoreSheetName() {
        return scoreSheetName;
    }

    public void setScoreSheetName(String scoreSheetName) {
        this.scoreSheetName = scoreSheetName;
    }
}