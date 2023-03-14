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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import simplelogin.listener.LogoutListener;
import simplelogin.listener.RequestListener;
import simplelogin.service.LogoutService;
import simplelogin.service.RequestService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
public final class RequestListenerTest {

    @Autowired
    private RequestListener requestListener;

    @MockBean
    private RequestService requestService;

    @MockBean(name = "request")
    private HttpServletRequest request;

    @MockBean(name = "response")
    private HttpServletResponse response;

    @MockBean(name = "filterChain")
    private FilterChain filterChain;

    @Test
    public void shouldAddNewUserLogoutMessageAndRedirect() throws IOException, ServletException {
        final Authentication authentication = createFakeAuthentication("admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(request.getRequestURL()).thenReturn(new StringBuffer("http://somesite.com/endpoint"));
        when(request.getRemoteHost()).thenReturn("192.168.2.4");
        when(request.getMethod()).thenReturn("GET");

        requestListener.doFilterInternal(request, response, filterChain);
        verify(requestService,
               times(1)).addUserRequest(1L,
                                                            "http://somesite.com/endpoint",
                                                            "192.168.2.4",
                                                            "GET");
        verify(filterChain,
               times(1)).doFilter(request, response);
    }

//    @Test()
//    public void shouldThrowErrorOnInvalidHttpRequest() {
//        final var thrown = assertThrows(
//                IllegalArgumentException.class,
//                () -> logoutListener.logout(null, response, createFakeAuthentication("admin")),
//                "shouldThrowErrorOnInvalidHttpRequest"
//        );
//
//        assertThat(thrown.getMessage()).isEqualTo("Logout can't be logged without HTTP request.");
//    }
//
//    @Test
//    public void shouldThrowErrorOnInvalidHttpResponse() {
//        final var thrown = assertThrows(
//                IllegalArgumentException.class,
//                () -> logoutListener.logout(request, null, createFakeAuthentication("admin")),
//                "shouldThrowErrorOnInvalidHttpResponse"
//        );
//
//        assertThat(thrown.getMessage()).isEqualTo("Logout can't be logged without HTTP response.");
//    }
//
//    @Test
//    public void shouldThrowErrorOnInvalidHttpAuthentication() {
//        final var thrown = assertThrows(
//                IllegalArgumentException.class,
//                () -> logoutListener.logout(request, response, null),
//                "shouldThrowErrorOnInvalidHttpAuthentication"
//        );
//
//        assertThat(thrown.getMessage()).isEqualTo("Logout can't be logged without Authentication.");
//    }
}