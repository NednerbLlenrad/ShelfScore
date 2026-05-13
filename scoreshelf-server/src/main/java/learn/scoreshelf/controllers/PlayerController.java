package learn.scoreshelf.controllers;

import jakarta.validation.Valid;
import learn.scoreshelf.domain.PlayerService;
import learn.scoreshelf.domain.Result;
import learn.scoreshelf.models.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/player")
@CrossOrigin(origins = {"http://localhost:5173"})
public class PlayerController {

    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Player> findAll() {
        return service.findAll();
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<Player> findById(@PathVariable int playerId) {

        Player player = service.findById(playerId);

        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(player);
    }

    @GetMapping("/app-user/{appUserId}")
    public List<Player> findByAppUserId(@PathVariable int appUserId) {
        return service.findByAppUserId(appUserId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody Player player) {

        Result<Player> result = service.add(player);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        URI location = URI.create(
                "/api/player/" + result.getPayload().getPlayerId()
        );

        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<Object> update(
            @PathVariable int playerId,
            @Valid @RequestBody Player player
    ) {

        if (playerId != player.getPlayerId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Player> result = service.update(player);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> deleteById(@PathVariable int playerId) {

        if (service.deleteById(playerId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}