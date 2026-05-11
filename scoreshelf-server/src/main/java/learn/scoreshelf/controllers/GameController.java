package learn.scoreshelf.controllers;

import jakarta.validation.Valid;
import learn.scoreshelf.domain.GameService;
import learn.scoreshelf.domain.Result;
import learn.scoreshelf.models.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = {"http://localhost:5173"})
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public List<Game> findAll() {
        return service.findAll();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> findById(@PathVariable int gameId) {
        Game game = service.findById(gameId);

        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(game);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody Game game) {
        Result<Game> result = service.add(game);

        if(!result.isSuccess()){
            return ErrorResponse.build(result);
        }

        URI location = URI.create("/api/game/" + result.getPayload().getGameId());
        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<Object> update(@PathVariable int gameId, @Valid @RequestBody Game game) {
        if (gameId != game.getGameId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Game> result = service.update(game);

        if(!result.isSuccess()){
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteById(@PathVariable int gameId){
        if (service.deleteById(gameId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}