package learn.scoreshelf.domain;

import learn.scoreshelf.data.AppUserRepository;
import learn.scoreshelf.models.AppUser;
import learn.scoreshelf.models.UpdateAccountRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository repository;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
    }

    public List<AppUser> findAll() {
        return repository.findAll();
    }

    public AppUser findById(int appUserId) {
        return repository.findById(appUserId);
    }

    public AppUser findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Result<AppUser> add(AppUser appUser) {

        Result<AppUser> result = validate(appUser);

        if (!result.isSuccess()) {
            return result;
        }

        if (appUser.getAppUserId() != 0) {
            result.addMessage("App user ID cannot be set for add operation.", ResultType.INVALID);
            return result;
        }

        appUser = repository.add(appUser);
        result.setPayload(appUser);

        return result;
    }

    public Result<AppUser> update(AppUser appUser) {

        Result<AppUser> result = validate(appUser);

        if (!result.isSuccess()) {
            return result;
        }

        if (appUser.getAppUserId() <= 0) {
            result.addMessage("App user ID is required for update.", ResultType.INVALID);
            return result;
        }

        if (!repository.update(appUser)) {
            result.addMessage("App user not found.", ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int appUserId) {
        return repository.deleteById(appUserId);
    }

    public Result<AppUser> updateAccount(UpdateAccountRequest request) {

        Result<AppUser> result = new Result<>();

        AppUser existingUser =
                repository.findById(request.getAppUserId());

        if (existingUser == null) {
            result.addMessage(
                    "User not found.",
                    ResultType.NOT_FOUND
            );

            return result;
        }

        existingUser.setUsername(request.getUsername());
        existingUser.setEmail(request.getEmail());

        if (request.getNewPassword() != null
                && !request.getNewPassword().isBlank()) {

            if (request.getCurrentPassword() == null
                    || request.getCurrentPassword().isBlank()) {

                result.addMessage(
                        "Current password is required.",
                        ResultType.INVALID
                );

                return result;
            }

            boolean matches = encoder.matches(
                    request.getCurrentPassword(),
                    existingUser.getPasswordHash()
            );

            if (!matches) {

                result.addMessage(
                        "Current password is incorrect.",
                        ResultType.INVALID
                );

                return result;
            }

            existingUser.setPasswordHash(
                    encoder.encode(request.getNewPassword())
            );
        }

        Result<AppUser> validation = validate(existingUser);

        if (!validation.isSuccess()) {
            return validation;
        }

        if (!repository.update(existingUser)) {

            result.addMessage(
                    "User not found.",
                    ResultType.NOT_FOUND
            );
        }

        result.setPayload(existingUser);

        return result;
    }

    //Helpers
    private Result<AppUser> validate(AppUser appUser) {

        Result<AppUser> result = new Result<>();

        if (appUser == null) {
            result.addMessage("App user cannot be null.", ResultType.INVALID);
            return result;
        }

        if (appUser.getUsername() == null || appUser.getUsername().isBlank()) {
            result.addMessage("Username is required.", ResultType.INVALID);
        }

        if (appUser.getUsername() != null && appUser.getUsername().length() > 30) {
            result.addMessage("Username must be 30 characters or less.", ResultType.INVALID);
        }

        if (appUser.getEmail() == null || appUser.getEmail().isBlank()) {
            result.addMessage("Email is required.", ResultType.INVALID);
        }

        if (appUser.getEmail() != null && appUser.getEmail().length() > 100) {
            result.addMessage("Email must be 100 characters or less.", ResultType.INVALID);
        }

        if (appUser.getPasswordHash() == null || appUser.getPasswordHash().isBlank()) {
            result.addMessage("Password hash is required.", ResultType.INVALID);
        }

        return result;
    }
}