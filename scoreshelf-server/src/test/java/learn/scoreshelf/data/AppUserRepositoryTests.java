package learn.scoreshelf.data;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.models.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserRepositoryTests {

    private final TestHelper helper = new TestHelper();

    @Autowired
    AppUserRepository repository;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    //Find
    @Test
    void shouldFindAll() {
        List<AppUser> users = repository.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void shouldFindById() {
        AppUser user = repository.findById(1);

        assertNotNull(user);
        assertEquals(1, user.getAppUserId());
        assertEquals("brenden", user.getUsername());
        assertEquals("brenden@test.com", user.getEmail());
    }

    @Test
    void shouldFindByUsername() {
        AppUser user = repository.findByUsername("brenden");

        assertNotNull(user);
        assertEquals(1, user.getAppUserId());
        assertEquals("brenden@test.com", user.getEmail());
    }

    @Test
    void shouldNotFindMissingUser() {
        AppUser user = repository.findById(999);

        assertNull(user);
    }

    //Add
    @Test
    void shouldAddUser() {
        AppUser user = helper.makeAppUser();

        AppUser actual = repository.add(user);

        assertNotNull(actual);
        assertTrue(actual.getAppUserId() > 0);

        AppUser saved = repository.findById(actual.getAppUserId());

        assertEquals("bob", saved.getUsername());
        assertEquals("bob@test.com", saved.getEmail());
    }

    //Update
    @Test
    void shouldUpdateUser() {
        AppUser user = repository.findById(1);

        user.setUsername("updated");
        user.setEmail("updated@test.com");

        assertTrue(repository.update(user));

        AppUser actual = repository.findById(1);

        assertEquals("updated", actual.getUsername());
        assertEquals("updated@test.com", actual.getEmail());
    }

    @Test
    void shouldNotUpdateMissingUser() {
        AppUser user = helper.makeAppUser();
        user.setAppUserId(999);

        assertFalse(repository.update(user));
    }

    //Delete
    @Test
    void shouldDeleteUser() {
        AppUser user = helper.makeAppUser();

        AppUser added = repository.add(user);

        assertTrue(repository.deleteById(added.getAppUserId()));
        assertNull(repository.findById(added.getAppUserId()));
    }

    @Test
    void shouldNotDeleteMissingUser() {
        assertFalse(repository.deleteById(999));
    }
}