package learn.scoreshelf.controllers;

import jakarta.validation.Valid;
import learn.scoreshelf.domain.AppUserService;
import learn.scoreshelf.domain.Result;
import learn.scoreshelf.models.AppUser;
import learn.scoreshelf.models.UpdateAccountRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/app-user")
@CrossOrigin(origins = {"http://localhost:5173"})
public class AppUserController {

    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppUser> findAll() {
        return service.findAll();
    }

    @GetMapping("/{appUserId}")
    public ResponseEntity<AppUser> findById(@PathVariable int appUserId) {

        AppUser appUser = service.findById(appUserId);

        if (appUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(appUser);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AppUser> findByUsername(@PathVariable String username) {

        AppUser appUser = service.findByUsername(username);

        if (appUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(appUser);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody AppUser appUser) {

        Result<AppUser> result = service.add(appUser);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        URI location = URI.create(
                "/api/app-user/" + result.getPayload().getAppUserId()
        );

        return ResponseEntity.created(location).body(result.getPayload());
    }

    @PutMapping("/{appUserId}")
    public ResponseEntity<Object> update(
            @PathVariable int appUserId,
            @RequestBody UpdateAccountRequest request
    ) {
        if (appUserId != request.getAppUserId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<AppUser> result = service.updateAccount(request);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{appUserId}")
    public ResponseEntity<Void> deleteById(@PathVariable int appUserId) {

        if (service.deleteById(appUserId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}