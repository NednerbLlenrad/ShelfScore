package learn.scoreshelf.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Player {

    private int playerId;

    @Min(value = 1, message = "App user ID is required.")
    private int appUserId;

    @NotBlank(message = "Player name is required.")
    @Size(max = 50, message = "Player name must be 50 characters or less.")
    private String playerName;

    private Integer linkedAppUserId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getLinkedAppUserId() {
        return linkedAppUserId;
    }

    public void setLinkedAppUserId(Integer linkedAppUserId) {
        this.linkedAppUserId = linkedAppUserId;
    }
}