package learn.scoreshelf.controllers;

import jakarta.validation.Valid;
import learn.scoreshelf.domain.Result;
import learn.scoreshelf.domain.ScoreEntryService;
import learn.scoreshelf.models.ScoreEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/score-entry")
@CrossOrigin(origins = {"http://localhost:5173"})
public class ScoreEntryController {

    private final ScoreEntryService service;

    public ScoreEntryController(ScoreEntryService service) {
        this.service = service;
    }

    @GetMapping
    public List<ScoreEntry> findAll() {
        return service.findAll();
    }

    @GetMapping("/{scoreEntryId}")
    public ResponseEntity<ScoreEntry> findById(@PathVariable int scoreEntryId) {

        ScoreEntry scoreEntry = service.findById(scoreEntryId);

        if (scoreEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(scoreEntry);
    }

    @GetMapping("/game-session-player/{gameSessionPlayerId}")
    public List<ScoreEntry> findByGameSessionPlayerId(
            @PathVariable int gameSessionPlayerId
    ) {
        return service.findByGameSessionPlayerId(gameSessionPlayerId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody ScoreEntry scoreEntry) {

        Result<ScoreEntry> result = service.add(scoreEntry);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        URI location = URI.create(
                "/api/score-entry/" + result.getPayload().getScoreEntryId()
        );

        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping("/{scoreEntryId}")
    public ResponseEntity<Object> update(
            @PathVariable int scoreEntryId,
            @Valid @RequestBody ScoreEntry scoreEntry
    ) {

        if (scoreEntryId != scoreEntry.getScoreEntryId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<ScoreEntry> result = service.update(scoreEntry);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{scoreEntryId}")
    public ResponseEntity<Void> deleteById(@PathVariable int scoreEntryId) {

        if (service.deleteById(scoreEntryId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}