package learn.scoreshelf.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Game {

    private int gameId;

    @NotBlank(message = "Game name is required.")
    @Size(max = 100, message = "Game name must be 100 characters or less.")
    private String gameName;

    @Size(max = 250, message = "Image URL must be 250 characters or less.")
    private String imageUrl;

    @NotBlank(message = "Category is required.")
    @Size(max = 50, message = "Category must be 50 characters or less.")
    private String category;

    @Min(value = 1, message = "Minimum players must be at least 1.")
    private int minPlayers;

    @Min(value = 1, message = "Maximum players must be at least 1.")
    @Max(value = 99, message = "Maximum players is too large.")
    private int maxPlayers;

    private boolean isPrivate;

    private int appUserId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }
}