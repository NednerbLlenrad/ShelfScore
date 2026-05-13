package learn.scoreshelf.data;

import learn.scoreshelf.models.GameSessionPlayer;

import java.util.List;

public interface GameSessionPlayerRepository {

    List<GameSessionPlayer> findAll();

    GameSessionPlayer findById(int gameSessionPlayerId);

    List<GameSessionPlayer> findByGameSessionId(int gameSessionId);

    GameSessionPlayer add(GameSessionPlayer player);

    boolean update(GameSessionPlayer player);

    boolean deleteById(int gameSessionPlayerId);
}