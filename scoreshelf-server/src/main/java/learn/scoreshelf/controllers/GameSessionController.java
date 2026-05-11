package learn.scoreshelf.controllers;

import jakarta.validation.Valid;
import learn.scoreshelf.domain.GameSessionService;
import learn.scoreshelf.domain.Result;
import learn.scoreshelf.models.GameSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/game-session")
@CrossOrigin(origins = {"http://localhost:5173"})
public class GameSessionController {

    private final GameSessionService service;

    public GameSessionController(GameSessionService service) {
        this.service = service;
    }

    @GetMapping
    public List<GameSession> findAll() {
        return service.findAll();
    }

    @GetMapping("/{gameSessionId}")
    public ResponseEntity<GameSession> findById(@PathVariable int gameSessionId) {

        GameSession session = service.findById(gameSessionId);

        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(session);
    }

    @GetMapping("/game/{gameId}")
    public List<GameSession> findByGameId(@PathVariable int gameId) {
        return service.findByGameId(gameId);
    }

    @GetMapping("/user/{appUserId}")
    public List<GameSession> findByAppUserId(@PathVariable int appUserId) {
        return service.findByAppUserId(appUserId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody GameSession session) {

        Result<GameSession> result = service.add(session);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        URI location = URI.create(
                "/api/game-session/" + result.getPayload().getGameSessionId()
        );

        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping("/{gameSessionId}")
    public ResponseEntity<Object> update(
            @PathVariable int gameSessionId,
            @Valid @RequestBody GameSession session
    ) {

        if (gameSessionId != session.getGameSessionId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<GameSession> result = service.update(session);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{gameSessionId}")
    public ResponseEntity<Void> deleteById(@PathVariable int gameSessionId) {

        if (service.deleteById(gameSessionId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}