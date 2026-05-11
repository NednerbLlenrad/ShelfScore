package learn.scoreshelf.data;

import learn.scoreshelf.models.GameSession;

import java.util.List;

public interface GameSessionRepository {

    List<GameSession> findAll();

    GameSession findById(int gameSessionId);

    List<GameSession> findByGameId(int gameId);

    List<GameSession> findByAppUserId(int appUserId);

    GameSession add(GameSession session);

    boolean update(GameSession session);

    boolean deleteById(int gameSessionId);
}