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
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import simplelogin.entity.Request;
import simplelogin.repository.RequestRepository;
import simplelogin.service.RequestService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith({ SpringExtension.class })
@AutoConfigureTestDatabase
public class RequestServiceTest {

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    @Sql(scripts = {
        "classpath:sql/tables.sql",
        "classpath:sql/clean.sql",
    })
    public void shouldAddUserRequestToDatabase() {

        final var userId = 1L;
        final var logoutReferer = "testUri/endpoint";
        final var userIp = "192.168.2.5";
        final var method = "POST";

        requestService.addUserRequest(userId,
                                     logoutReferer,
                                     userIp,
                                     method);

        final var logoutNotifications = requestRepository.findAll();
        assertThat(logoutNotifications).hasSize(1);
        assertThat(logoutNotifications.get(0)
                                     .getRequestDate()).isNotNull();
        assertThat(logoutNotifications).extracting(Request::getUserId,
                                                   Request::getUri,
                                                   Request::getIp,
                                                   Request::getMethod)
                                      .containsExactly(tuple(userId,
                                                             logoutReferer,
                                                             userIp,
                                                             method));

    }

    @Test()
    public void shouldThrowErrorOnInvalidUserId() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> requestService.addUserRequest(-2L, "uri/", "192.168.2.1", "POST"),
                "shouldThrowErrorOnInvalidUserId"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid User Id must be provided when adding user request.");
    }

    @Test()
    public void shouldThrowErrorOnInvalidReferer() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> requestService.addUserRequest(1L, "", "192.168.2.1", "POST"),
                "shouldThrowErrorOnInvalidReferer"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid Url must be provided when adding user request.");
    }

    @Test()
    public void shouldThrowErrorOnInvalidUserIp() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> requestService.addUserRequest(1L, "uri/", "", "POST"),
                "shouldThrowErrorOnInvalidUserIp"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid User Ip must be provided when adding user request.");
    }

    @Test()
    public void shouldThrowErrorOnInvalidBlankHttpVerb() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> requestService.addUserRequest(1L, "uri/", "192.168.2.1", ""),
                "shouldThrowErrorOnInvalidBlankHttpVerb"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid HTTP Method must be provided when adding user request.");
    }

    @Test()
    public void shouldThrowErrorOnInvalidHttpVerb() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () ->         requestService.addUserRequest(1L, "uri/", "192.168.2.1", "error"),
                "shouldThrowErrorOnInvalidBlankHttpVerb"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid HTTP Method must be provided when adding user request.");
    }
}
