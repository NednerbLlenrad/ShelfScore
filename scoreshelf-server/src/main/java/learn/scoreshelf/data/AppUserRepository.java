package learn.scoreshelf.data;

import learn.scoreshelf.models.AppUser;

import java.util.List;

public interface AppUserRepository {

    List<AppUser> findAll();

    AppUser findById(int appUserId);

    AppUser findByUsername(String username);

    AppUser findByEmail(String email);

    AppUser add(AppUser appUser);

    boolean update(AppUser appUser);

    boolean deleteById(int appUserId);
}