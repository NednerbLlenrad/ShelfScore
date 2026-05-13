package learn.scoreshelf.domain;

import learn.scoreshelf.data.AppUserRepository;
import learn.scoreshelf.models.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthService(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder,
            JWTService jwtService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Result<AuthResponse> register(RegisterRequest registerRequest){
        Result<AuthResponse> result = new Result<>();

        if(registerRequest == null) {
            result.addMessage("Registration request cannot be null.", ResultType.INVALID);
            return result;
        }

        if(registerRequest.getUsername() == null || registerRequest.getUsername().isBlank()){
            result.addMessage("Username is required.", ResultType.INVALID);
        }

        if(registerRequest.getEmail() == null || registerRequest.getEmail().isBlank()){
            result.addMessage("Email is required.", ResultType.INVALID);
        }

        if(registerRequest.getPassword() == null || registerRequest.getPassword().isBlank()) {
            result.addMessage("Password is required.", ResultType.INVALID);
        }

        if (registerRequest.getPassword().length() < 8) {
            result.addMessage("Password must be at least 8 characters.", ResultType.INVALID);
        }

        if(!result.isSuccess()){
            return result;
        }

        if (repository.findByUsername(registerRequest.getUsername()) != null){
            result.addMessage("Username is already taken", ResultType.INVALID);
            return result;
        }

        if (repository.findByEmail(registerRequest.getEmail()) != null) {
            result.addMessage("Email is already in use.", ResultType.INVALID);
            return result;
        }

        AppUser user = new AppUser();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));

        user = repository.add(user);
        result.setPayload(makeAuthResponse(user));

        return result;
    }

    public Result<AuthResponse> login(LoginRequest request) {

        Result<AuthResponse> result = new Result<>();

        if (request == null) {
            result.addMessage("Login request cannot be null.", ResultType.INVALID);
            return result;
        }

        AppUser user = repository.findByUsername(request.getUsername());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            result.addMessage("Invalid username or password.", ResultType.INVALID);
            return result;
        }

        if (request.getUsername() == null || request.getUsername().isBlank()) {
            result.addMessage("Username is required.", ResultType.INVALID);
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            result.addMessage("Password is required.", ResultType.INVALID);
        }

        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(makeAuthResponse(user));
        return result;
    }

    private AppUserResponse makeResponse(AppUser user) {

        AppUserResponse response = new AppUserResponse();

        response.setAppUserId(user.getAppUserId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }

    private AuthResponse makeAuthResponse(AppUser user) {

        AuthResponse response = new AuthResponse();

        response.setToken(jwtService.generateToken(user));
        response.setAppUser(makeResponse(user));

        return response;
    }
}
