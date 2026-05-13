package learn.scoreshelf.domain;

import learn.scoreshelf.TestHelper;
import learn.scoreshelf.data.AppUserRepository;
import learn.scoreshelf.models.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AppUserServiceTests {

    private final TestHelper helper = new TestHelper();

    @Autowired
    AppUserService service;

    @MockitoBean
    AppUserRepository repository;

    //Find
    @Test
    void shouldFindAll() {
        List<AppUser> expected = List.of(helper.makeAppUser());

        when(repository.findAll()).thenReturn(expected);

        List<AppUser> actual = service.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        AppUser expected = helper.makeAppUser();
        expected.setAppUserId(1);

        when(repository.findById(1)).thenReturn(expected);

        AppUser actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindByUsername() {
        AppUser expected = helper.makeAppUser();

        when(repository.findByUsername("jake")).thenReturn(expected);

        AppUser actual = service.findByUsername("jake");

        assertEquals(expected, actual);
    }

    //Add
    @Test
    void shouldAddUser() {
        AppUser arg = helper.makeAppUser();

        AppUser expected = helper.makeAppUser();
        expected.setAppUserId(5);

        when(repository.add(arg)).thenReturn(expected);

        Result<AppUser> result = service.add(arg);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddNullUser() {
        Result<AppUser> result = service.add(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenIdIsSet() {
        AppUser user = helper.makeAppUser();
        user.setAppUserId(1);

        Result<AppUser> result = service.add(user);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(user);
    }

    @Test
    void shouldNotAddWithoutUsername() {
        AppUser user = helper.makeAppUser();
        user.setUsername(null);

        Result<AppUser> result = service.add(user);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(user);
    }

    @Test
    void shouldNotAddWithoutEmail() {
        AppUser user = helper.makeAppUser();
        user.setEmail(null);

        Result<AppUser> result = service.add(user);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(user);
    }

    @Test
    void shouldNotAddWithoutPasswordHash() {
        AppUser user = helper.makeAppUser();
        user.setPasswordHash(null);

        Result<AppUser> result = service.add(user);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).add(user);
    }

    //Update
    @Test
    void shouldUpdateUser() {
        AppUser user = helper.makeAppUser();
        user.setAppUserId(1);

        when(repository.update(user)).thenReturn(true);

        Result<AppUser> result = service.update(user);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateNullUser() {
        Result<AppUser> result = service.update(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        AppUser user = helper.makeAppUser();

        Result<AppUser> result = service.update(user);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        verify(repository, never()).update(user);
    }

    @Test
    void shouldNotUpdateMissingUser() {
        AppUser user = helper.makeAppUser();
        user.setAppUserId(999);

        when(repository.update(user)).thenReturn(false);

        Result<AppUser> result = service.update(user);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    //Delete
    @Test
    void shouldDeleteUser() {
        when(repository.deleteById(1)).thenReturn(true);

        assertTrue(service.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissingUser() {
        when(repository.deleteById(999)).thenReturn(false);

        assertFalse(service.deleteById(999));
    }
}