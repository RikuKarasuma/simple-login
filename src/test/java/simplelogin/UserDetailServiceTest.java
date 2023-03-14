/**********************************
*              @2023              *
**********************************/
package simplelogin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import simplelogin.repository.UserRepository;
import simplelogin.service.UserDetailService;

import java.io.IOException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith({ SpringExtension.class })
@AutoConfigureTestDatabase
public class UserDetailServiceTest {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(scripts={
        "classpath:sql/clean.sql",
        "classpath:sql/GenericData.sql"
    })
    public void shouldLoadExistingUserFromDatabase() {
        final var username = "admin";
        final var user = userDetailService.loadUserByUsername(username);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
        final var samePassword = new BCryptPasswordEncoder().matches(username, user.getPassword());
        assertThat(samePassword).isTrue();
    }

    @Test
    @Sql({
        "classpath:sql/clean.sql",
    })
    public void shouldCreateNewUserFromRequest() throws IOException {
        final var parameterMap = new HashMap<String, String>();
        final var usernamePassword = "admin";
        parameterMap.put("username", usernamePassword);
        parameterMap.put("password", usernamePassword);
        final var request = TestUtils.createFakeServletRequest(parameterMap);
        final var response = TestUtils.createFakeServletResponse();
        userDetailService.createNewUser(request, response);
        final var newlyCreatedUser = userRepository.getUser(usernamePassword);
        assertThat(newlyCreatedUser).isNotNull();
    }


    @Test
    @Sql({
        "classpath:sql/clean.sql",
    })
    public void shouldCreateHashedPasswordFromNewUserRequest() throws IOException {
        final var parameterMap = new HashMap<String, String>();
        final var usernamePassword = "admin";
        parameterMap.put("username", usernamePassword);
        parameterMap.put("password", usernamePassword);
        final var request = TestUtils.createFakeServletRequest(parameterMap);
        final var response = TestUtils.createFakeServletResponse();
        userDetailService.createNewUser(request, response);
        final var newlyCreatedUser = userRepository.getUser(usernamePassword).orElse(null);
        assertThat(newlyCreatedUser).isNotNull();
        final var samePassword = new BCryptPasswordEncoder().matches(usernamePassword, newlyCreatedUser.getPassword());
        assertThat(samePassword).isTrue();
    }

    @Test()
    public void shouldReturnNullOnInvalidUsernameWhenLoadingUser() {
        assertThat(userDetailService.loadUserByUsername("")).isNull();
    }

    @Test()
    public void shouldThrowErrorOnInvalidRequestWhenCreatingUser() throws IOException {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userDetailService.createNewUser(null, null),
                "shouldThrowErrorOnInvalidRequestWhenCreatingUser"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid request must be provided when creating a user.");
    }

    @Test()
    public void shouldThrowErrorOnInvalidResponseWhenCreatingUser() throws IOException {
        final var request = TestUtils.createFakeServletRequest(null);
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userDetailService.createNewUser(request, null),
                "shouldThrowErrorOnInvalidResponseWhenCreatingUser"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid response must be provided when creating a user.");
    }

    @Test()
    public void shouldThrowErrorOnInvalidUsernameWhenCreatingUser() throws IOException {
        final var parameterMap = new HashMap<String, String>();
        final var usernamePassword = "admin";
        parameterMap.put("username", null);
        parameterMap.put("password", usernamePassword);
        final var request = TestUtils.createFakeServletRequest(parameterMap);
        final var response = TestUtils.createFakeServletResponse();
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userDetailService.createNewUser(request, response),
                "shouldThrowErrorOnInvalidUsernameWhenCreatingUser"
        );

        assertThat(thrown.getMessage()).isEqualTo("Username must be provided when creating a user.");
    }

    @Test()
    public void shouldThrowErrorOnInvalidPasswordWhenCreatingUser() throws IOException {
        final var parameterMap = new HashMap<String, String>();
        final var usernamePassword = "admin";
        parameterMap.put("username", usernamePassword);
        parameterMap.put("password", null);
        final var request = TestUtils.createFakeServletRequest(parameterMap);
        final var response = TestUtils.createFakeServletResponse();
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userDetailService.createNewUser(request, response),
                "shouldThrowErrorOnInvalidUsernameWhenCreatingUser"
        );

        assertThat(thrown.getMessage()).isEqualTo("Password must be provided when creating a user.");
    }
}
