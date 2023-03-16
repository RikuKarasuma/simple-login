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
import simplelogin.entity.Logout;
import simplelogin.repository.LogoutRepository;
import simplelogin.service.LogoutService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith({ SpringExtension.class})
@AutoConfigureTestDatabase
public class LogoutServiceTest {

    @Autowired
    private LogoutService logoutService;

    @Autowired
    private LogoutRepository logoutRepository;

    @Test
    @Sql(scripts = {
        "classpath:sql/tables.sql",
        "classpath:sql/clean.sql",
    })
    public void shouldAddUserLogoutToDatabase() {

        final var userId = 1L;
        final var logoutReferer = "testUri/endpoint";
        final var userIp = "192.168.2.5";

        logoutService.addUserLogout(userId,
                                    logoutReferer,
                                    userIp);

        final var logoutNotifications = logoutRepository.findAll();
        assertThat(logoutNotifications).hasSize(1);
        assertThat(logoutNotifications.get(0)
                                     .getLogoutDate()).isNotNull();
        assertThat(logoutNotifications).extracting(Logout::getUserId,
                                                   Logout::getUri,
                                                   Logout::getIp)
                                      .containsExactly(tuple(userId,
                                                             logoutReferer,
                                                             userIp));

    }

    @Test
    public void shouldThrowErrorOnInvalidUserId() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> logoutService.addUserLogout(-1L, "uri/", "192.168.2.1"),
                "shouldThrowErrorOnInvalidUserId"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid User Id must be provided when adding user logout.");
    }

    @Test
    public void shouldThrowErrorOnInvalidReferer() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> logoutService.addUserLogout(1L, "", "192.168.2.1"),
                "shouldThrowErrorOnInvalidReferer"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid Referer must be provided when adding user logout.");
    }

    @Test
    public void shouldThrowErrorOnInvalidUserIp() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> logoutService.addUserLogout(1L, "uri/", ""),
                "shouldThrowErrorOnInvalidUserIp"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid User Ip must be provided when adding user logout.");
    }
}
