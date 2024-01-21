/**********************************
*              @2023              *
**********************************/
package simplelogin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import simplelogin.entity.Login;
import simplelogin.repository.LoginRepository;
import simplelogin.service.LoginService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@SpringBootTest
@ExtendWith({ SpringExtension.class})
@AutoConfigureTestDatabase
public class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private LoginRepository loginRepository;

    @Test
    @Sql(scripts = {
        "classpath:sql/tables.sql",
        "classpath:sql/clean.sql",
    })
    public void shouldAddUserLoginToDatabase() {

        final var userId = 1L;
        final var loginReferer = "testUri/endpoint";
        final var userIp = "192.168.2.5";
        final var attemptSuccessful = true;

        loginService.addUserLogin(userId,
                                  loginReferer,
                                  userIp,
                                  attemptSuccessful);

        final var loginNotifications = loginRepository.findAll();
        assertThat(loginNotifications).hasSize(1);
        assertThat(loginNotifications.get(0)
                                     .getLoginDate()).isNotNull();
        assertThat(loginNotifications).extracting(Login::getUserId,
                                                  Login::getUri,
                                                  Login::getIp,
                                                  Login::isSuccess)
                                      .containsExactly(tuple(userId,
                                                             loginReferer,
                                                             userIp,
                                                             attemptSuccessful));

    }

    @Test()
    public void shouldThrowErrorOnInvalidUserLoginId() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> loginService.addUserLogin(-1L, "uri/", "192.168.2.1", false),
                "shouldThrowErrorOnInvalidUserLoginId"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid User Id must be provided when adding user login.");
    }

    @Test()
    public void shouldThrowErrorOnInvalidReferer() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> loginService.addUserLogin(1L, "", "192.168.2.1", false),
                "shouldThrowErrorOnInvalidReferer"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid Referer must be provided when adding user login.");
    }

    @Test()
    public void shouldThrowErrorOnInvalidUserIp() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> loginService.addUserLogin(1L, "uri/", "", false),
                "shouldThrowErrorOnInvalidUserIp"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid User Ip must be provided when adding user login.");
    }
}
