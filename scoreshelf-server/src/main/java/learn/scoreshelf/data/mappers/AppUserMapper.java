package learn.scoreshelf.data.mappers;

import learn.scoreshelf.models.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppUserMapper implements RowMapper<AppUser> {

    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {

        AppUser appUser = new AppUser();

        appUser.setAppUserId(rs.getInt("app_user_id"));
        appUser.setUsername(rs.getString("username"));
        appUser.setEmail(rs.getString("email"));
        appUser.setPasswordHash(rs.getString("password_hash"));

        return appUser;
    }
}