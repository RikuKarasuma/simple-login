/**********************************
*              @2023              *
**********************************/
package simplelogin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import simplelogin.listener.LogoutListener;
import simplelogin.service.LogoutService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static simplelogin.TestUtils.createFakeAuthentication;

@SpringBootTest()
@ExtendWith({ MockitoExtension.class, SpringExtension.class})
@AutoConfigureTestDatabase
public final class LogoutListenerTest {

    @Autowired
    private LogoutListener logoutListener;

    @MockBean
    private LogoutService logoutService;

    @MockBean(name = "request")
    private HttpServletRequest request;

    @MockBean(name = "response")
    private HttpServletResponse response;

    @Test
    @Sql(scripts = {
        "classpath:sql/tables.sql",
        "classpath:sql/clean.sql",
    })
    public void shouldAddNewUserLogoutMessageAndRedirect() throws IOException {
        final Authentication authentication = createFakeAuthentication("admin");

        when(request.getHeader("referer")).thenReturn("http://somesite.com/endpoint");
        when(request.getRemoteHost()).thenReturn("192.168.2.4");

        logoutListener.logout(request, response, authentication);
        verify(logoutService,
               times(1)).addUserLogout(1L,
                                                            "http://somesite.com/endpoint",
                                                            "192.168.2.4");
        verify(response,
               times(1)).sendRedirect("/Login.html");
    }

    @Test()
    public void shouldThrowErrorOnInvalidHttpRequest() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> logoutListener.logout(null, response, createFakeAuthentication("admin")),
                "shouldThrowErrorOnInvalidHttpRequest"
        );

        assertThat(thrown.getMessage()).isEqualTo("Logout can't be logged without HTTP request.");
    }

    @Test
    public void shouldThrowErrorOnInvalidHttpResponse() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> logoutListener.logout(request, null, createFakeAuthentication("admin")),
                "shouldThrowErrorOnInvalidHttpResponse"
        );

        assertThat(thrown.getMessage()).isEqualTo("Logout can't be logged without HTTP response.");
    }

    @Test
    public void shouldThrowErrorOnInvalidHttpAuthentication() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> logoutListener.logout(request, response, null),
                "shouldThrowErrorOnInvalidHttpAuthentication"
        );

        assertThat(thrown.getMessage()).isEqualTo("Logout can't be logged without Authentication.");
    }
}