package learn.scoreshelf.controllers;

import jakarta.validation.Valid;
import learn.scoreshelf.domain.ScoreSheetService;
import learn.scoreshelf.domain.Result;
import learn.scoreshelf.models.ScoreSheet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/score-sheet")
@CrossOrigin(origins = {"http://localhost:5173"})
public class ScoreSheetController {

    private final ScoreSheetService service;

    public ScoreSheetController(ScoreSheetService service) {
        this.service = service;
    }

    @GetMapping
    public List<ScoreSheet> findAll() {
        return service.findAll();
    }

    @GetMapping("/{scoreSheetId}")
    public ResponseEntity<ScoreSheet> findById(@PathVariable int scoreSheetId) {
        ScoreSheet scoreSheet = service.findById(scoreSheetId);

        if (scoreSheet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(scoreSheet);
    }

    @GetMapping("/game/{gameId}")
    public List<ScoreSheet> findByGameId(@PathVariable int gameId) {
        return service.findByGameId(gameId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody ScoreSheet scoreSheet) {
        Result<ScoreSheet> result = service.add(scoreSheet);

        if(!result.isSuccess()){
            return ErrorResponse.build(result);
        }

        URI location = URI.create("/api/score-sheet/" + result.getPayload().getScoreSheetId());
        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping("/{scoreSheetId}")
    public ResponseEntity<Object> update(@PathVariable int scoreSheetId, @Valid @RequestBody ScoreSheet scoreSheet) {
        if (scoreSheetId != scoreSheet.getScoreSheetId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<ScoreSheet> result = service.update(scoreSheet);

        if(!result.isSuccess()){
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{scoreSheetId}")
    public ResponseEntity<Void> deleteById(@PathVariable int scoreSheetId){
        if (service.deleteById(scoreSheetId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}