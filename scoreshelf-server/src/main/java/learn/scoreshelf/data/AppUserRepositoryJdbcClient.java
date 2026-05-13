package learn.scoreshelf.data;

import learn.scoreshelf.data.mappers.AppUserMapper;
import learn.scoreshelf.models.AppUser;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppUserRepositoryJdbcClient implements AppUserRepository {

    private final JdbcClient jdbcClient;

    public AppUserRepositoryJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<AppUser> findAll() {

        final String sql = """
                select
                    app_user_id,
                    username,
                    email,
                    password_hash
                from app_user;
                """;

        return jdbcClient.sql(sql)
                .query(new AppUserMapper())
                .list();
    }

    @Override
    public AppUser findById(int appUserId) {

        final String sql = """
                select
                    app_user_id,
                    username,
                    email,
                    password_hash
                from app_user
                where app_user_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(appUserId)
                .query(new AppUserMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public AppUser findByUsername(String username) {

        final String sql = """
                select
                    app_user_id,
                    username,
                    email,
                    password_hash
                from app_user
                where username = ?;
                """;

        return jdbcClient.sql(sql)
                .param(username)
                .query(new AppUserMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public AppUser findByEmail(String email) {

        final String sql = """
            select
                app_user_id,
                username,
                email,
                password_hash
            from app_user
            where email = ?;
            """;

        return jdbcClient.sql(sql)
                .param(email)
                .query(new AppUserMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public AppUser add(AppUser appUser) {

        final String sql = """
                insert into app_user (
                    username,
                    email,
                    password_hash
                )
                values (?, ?, ?);
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param(appUser.getUsername())
                .param(appUser.getEmail())
                .param(appUser.getPasswordHash())
                .update(keyHolder);

        appUser.setAppUserId(keyHolder.getKey().intValue());

        return appUser;
    }

    @Override
    public boolean update(AppUser appUser) {

        final String sql = """
                update app_user set
                    username = ?,
                    email = ?,
                    password_hash = ?
                where app_user_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(appUser.getUsername())
                .param(appUser.getEmail())
                .param(appUser.getPasswordHash())
                .param(appUser.getAppUserId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int appUserId) {

        final String sql = """
                delete from app_user
                where app_user_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(appUserId)
                .update() > 0;
    }
}