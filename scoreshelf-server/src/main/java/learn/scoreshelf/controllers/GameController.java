package learn.scoreshelf.controllers;

import jakarta.validation.Valid;
import learn.scoreshelf.domain.GameService;
import learn.scoreshelf.domain.Result;
import learn.scoreshelf.models.AppUser;
import learn.scoreshelf.models.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
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

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> findById(@PathVariable int gameId) {
        Game game = service.findById(gameId);

        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(game);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> add(
            @RequestParam String gameName,
            @RequestParam String category,
            @RequestParam int minPlayers,
            @RequestParam int maxPlayers,
            @RequestParam boolean isPrivate,
            @RequestParam int appUserId,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {

        Game game = new Game();

        game.setGameName(gameName);
        game.setCategory(category);
        game.setMinPlayers(minPlayers);
        game.setMaxPlayers(maxPlayers);
        game.setPrivate(isPrivate);
        game.setAppUserId(appUserId);

        if (image != null && !image.isEmpty()) {

            if (!"image/png".equals(image.getContentType())) {
                return ResponseEntity.badRequest()
                        .body("Only PNG files are allowed.");
            }

            long maxSize = 2 * 1024 * 1024;

            if (image.getSize() > maxSize) {
                return ResponseEntity.badRequest()
                        .body("Image must be under 2MB.");
            }

            Path uploadPath = Paths.get("uploads/games");

            Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(image.getInputStream(), filePath);

            game.setImageUrl("/uploads/games/" + fileName);
        }

        Result<Game> result = service.add(game);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        URI location = URI.create("/api/game/" + result.getPayload().getGameId());

        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping(
            value = "/{gameId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Object> update(
            @PathVariable int gameId,
            @RequestParam String gameName,
            @RequestParam String category,
            @RequestParam int minPlayers,
            @RequestParam int maxPlayers,
            @RequestParam boolean isPrivate,
            @RequestParam int appUserId,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {

        Game game = new Game();

        game.setGameId(gameId);
        game.setGameName(gameName);
        game.setCategory(category);
        game.setMinPlayers(minPlayers);
        game.setMaxPlayers(maxPlayers);
        game.setPrivate(isPrivate);
        game.setAppUserId(appUserId);
        game.setImageUrl(imageUrl);

        if (image != null && !image.isEmpty()) {
            if (!"image/png".equals(image.getContentType())) {
                return ResponseEntity.badRequest()
                        .body("Only PNG files are allowed.");
            }

            long maxSize = 2 * 1024 * 1024;

            if (image.getSize() > maxSize) {
                return ResponseEntity.badRequest()
                        .body("Image must be under 2MB.");
            }

            Path uploadPath = Paths.get("uploads/games");
            Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(image.getInputStream(), filePath);

            game.setImageUrl("/uploads/games/" + fileName);
        }

        Result<Game> result = service.update(game);

        if (!result.isSuccess()) {
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

    @GetMapping
    public List<Game> findAll(Authentication authentication) {

        if (authentication == null) {
            return service.findPublicGames();
        }

        AppUser user = (AppUser) authentication.getPrincipal();

        return service.findAccessibleGames(user.getAppUserId());
    }

    @GetMapping("/my")
    public List<Game> findMyGames(Authentication authentication) {
        AppUser user = (AppUser) authentication.getPrincipal();

        return service.findByAppUserId(user.getAppUserId());
    }
}