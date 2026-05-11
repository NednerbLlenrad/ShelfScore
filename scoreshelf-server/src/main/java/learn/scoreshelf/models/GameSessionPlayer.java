package learn.scoreshelf.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GameSessionPlayer {

    private int gameSessionPlayerId;

    @Min(value = 1, message = "Game session ID is required.")
    private int gameSessionId;

    private Integer playerId;

    @NotBlank(message = "Player name is required.")
    @Size(max = 50, message = "Player name must be 50 characters or less.")
    private String playerName;

    @Min(value = 0, message = "Total score cannot be negative.")
    private int totalScore;

    @NotNull(message = "Winner status is required.")
    private Boolean isWinner;

    public int getGameSessionPlayerId() {
        return gameSessionPlayerId;
    }

    public void setGameSessionPlayerId(int gameSessionPlayerId) {
        this.gameSessionPlayerId = gameSessionPlayerId;
    }

    public int getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(int gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public Boolean getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(Boolean winner) {
        isWinner = winner;
    }
}