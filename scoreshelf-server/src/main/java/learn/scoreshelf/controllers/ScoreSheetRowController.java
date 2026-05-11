package learn.scoreshelf.controllers;

import jakarta.validation.Valid;
import learn.scoreshelf.domain.Result;
import learn.scoreshelf.domain.ScoreSheetRowService;
import learn.scoreshelf.models.ScoreSheetRow;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/score-sheet-row")
@CrossOrigin(origins = {"http://localhost:5173"})
public class ScoreSheetRowController {

    private final ScoreSheetRowService service;

    public ScoreSheetRowController(ScoreSheetRowService service) {
        this.service = service;
    }

    @GetMapping
    public List<ScoreSheetRow> findAll() {
        return service.findAll();
    }

    @GetMapping("/{scoreSheetRowId}")
    public ResponseEntity<ScoreSheetRow> findById(@PathVariable int scoreSheetRowId) {

        ScoreSheetRow row = service.findById(scoreSheetRowId);

        if (row == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(row);
    }

    @GetMapping("/score-sheet/{scoreSheetId}")
    public List<ScoreSheetRow> findByScoreSheetId(@PathVariable int scoreSheetId) {
        return service.findByScoreSheetId(scoreSheetId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody ScoreSheetRow row) {

        Result<ScoreSheetRow> result = service.add(row);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        URI location = URI.create(
                "/api/score-sheet-row/" + result.getPayload().getScoreSheetRowId()
        );

        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping("/{scoreSheetRowId}")
    public ResponseEntity<Object> update(
            @PathVariable int scoreSheetRowId,
            @Valid @RequestBody ScoreSheetRow row
    ) {

        if (scoreSheetRowId != row.getScoreSheetRowId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<ScoreSheetRow> result = service.update(row);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{scoreSheetRowId}")
    public ResponseEntity<Void> deleteById(@PathVariable int scoreSheetRowId) {

        if (service.deleteById(scoreSheetRowId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}