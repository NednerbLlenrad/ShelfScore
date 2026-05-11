package learn.scoreshelf.controllers;

import jakarta.validation.Valid;
import learn.scoreshelf.domain.GameSessionPlayerService;
import learn.scoreshelf.domain.Result;
import learn.scoreshelf.models.GameSessionPlayer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/game-session-player")
@CrossOrigin(origins = {"http://localhost:5173"})
public class GameSessionPlayerController {

    private final GameSessionPlayerService service;

    public GameSessionPlayerController(GameSessionPlayerService service) {
        this.service = service;
    }

    @GetMapping
    public List<GameSessionPlayer> findAll() {
        return service.findAll();
    }

    @GetMapping("/{gameSessionPlayerId}")
    public ResponseEntity<GameSessionPlayer> findById(@PathVariable int gameSessionPlayerId) {

        GameSessionPlayer player = service.findById(gameSessionPlayerId);

        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(player);
    }

    @GetMapping("/game-session/{gameSessionId}")
    public List<GameSessionPlayer> findByGameSessionId(@PathVariable int gameSessionId) {
        return service.findByGameSessionId(gameSessionId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody GameSessionPlayer player) {

        Result<GameSessionPlayer> result = service.add(player);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        URI location = URI.create(
                "/api/game-session-player/" + result.getPayload().getGameSessionPlayerId()
        );

        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping("/{gameSessionPlayerId}")
    public ResponseEntity<Object> update(
            @PathVariable int gameSessionPlayerId,
            @Valid @RequestBody GameSessionPlayer player
    ) {

        if (gameSessionPlayerId != player.getGameSessionPlayerId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<GameSessionPlayer> result = service.update(player);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{gameSessionPlayerId}")
    public ResponseEntity<Void> deleteById(@PathVariable int gameSessionPlayerId) {

        if (service.deleteById(gameSessionPlayerId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}