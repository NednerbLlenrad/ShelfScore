package learn.scoreshelf.controllers;

import learn.scoreshelf.domain.AuthService;
import learn.scoreshelf.domain.Result;
import learn.scoreshelf.models.AppUserResponse;
import learn.scoreshelf.models.LoginRequest;
import learn.scoreshelf.models.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173"})
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        Result<AppUserResponse> result = service.register(request);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return ResponseEntity.ok(result.getPayload());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        Result<AppUserResponse> result = service.login(request);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return ResponseEntity.ok(result.getPayload());
    }
}