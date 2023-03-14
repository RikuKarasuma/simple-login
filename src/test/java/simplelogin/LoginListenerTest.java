/**********************************
*              @2023              *
**********************************/
package simplelogin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import simplelogin.listener.LoginListener;
import simplelogin.service.LoginService;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;
import static simplelogin.TestUtils.createFakeAuthenticationEvent;

@SpringBootTest()
@ExtendWith({ MockitoExtension.class, SpringExtension.class})
public final class LoginListenerTest {

    @Autowired
    private LoginListener loginListener;

    @MockBean
    private LoginService loginService;

    @MockBean(name = "request")
    private HttpServletRequest request;

    @Test
    public void shouldAddNewUserLoginMessage() {
        final AuthenticationSuccessEvent loginEvent = ((AuthenticationSuccessEvent)createFakeAuthenticationEvent("admin"));

        when(request.getHeader("referer")).thenReturn("http://somesite.com/endpoint");
        when(request.getRemoteHost()).thenReturn("192.168.2.4");

        loginListener.onApplicationEvent(loginEvent);
        verify(loginService,
               times(1)).addUserLogin(1L,
                                                            "http://somesite.com/endpoint",
                                                            "192.168.2.4",
                                                            true);
    }

    @Test
    public void shouldReturnEarlyOnNonSuccessEvent() {
        final var loginEvent = createFakeAuthenticationEvent();

        loginListener.onApplicationEvent(loginEvent);
        verify(loginService,
               times(0)).addUserLogin(1L,
                                                            null,
                                                            null,
                                                            true);
    }
}