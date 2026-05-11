package learn.scoreshelf.domain;

import learn.scoreshelf.data.GameSessionRepository;
import learn.scoreshelf.models.GameSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameSessionService {

    private final GameSessionRepository repository;

    public GameSessionService(GameSessionRepository repository) {
        this.repository = repository;
    }

    public List<GameSession> findAll() {
        return repository.findAll();
    }

    public GameSession findById(int gameSessionId) {
        return repository.findById(gameSessionId);
    }

    public List<GameSession> findByGameId(int gameId) {
        return repository.findByGameId(gameId);
    }

    public List<GameSession> findByAppUserId(int appUserId) {
        return repository.findByAppUserId(appUserId);
    }

    public Result<GameSession> add(GameSession session) {

        Result<GameSession> result = validate(session);

        if (!result.isSuccess()) {
            return result;
        }

        if (session.getGameSessionId() != 0) {
            result.addMessage("Game session ID cannot be set for add operation.", ResultType.INVALID);
            return result;
        }

        session = repository.add(session);
        result.setPayload(session);

        return result;
    }

    public Result<GameSession> update(GameSession session) {

        Result<GameSession> result = validate(session);

        if (!result.isSuccess()) {
            return result;
        }

        if (session.getGameSessionId() <= 0) {
            result.addMessage("Game session ID is required for update.", ResultType.INVALID);
            return result;
        }

        if (!repository.update(session)) {
            result.addMessage("Game session not found.", ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int gameSessionId) {
        return repository.deleteById(gameSessionId);
    }

    //Helpers
    private Result<GameSession> validate(GameSession session) {

        Result<GameSession> result = new Result<>();

        if (session == null) {
            result.addMessage("Game session cannot be null.", ResultType.INVALID);
            return result;
        }

        if (session.getGameId() <= 0) {
            result.addMessage("Game ID is required.", ResultType.INVALID);
        }

        if (session.getAppUserId() <= 0) {
            result.addMessage("App user ID is required.", ResultType.INVALID);
        }

        if (session.getPlayedAt() == null) {
            result.addMessage("Played at date is required.", ResultType.INVALID);
        }

        return result;
    }
}