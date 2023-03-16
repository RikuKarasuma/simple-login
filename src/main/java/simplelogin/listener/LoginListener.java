/**********************************
*              @2023              *
**********************************/
package simplelogin.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import simplelogin.entity.User;
import simplelogin.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

import static simplelogin.RequestUtils.makeReferrer;
import static simplelogin.RequestUtils.setTimeoutOnRequestSession;

@Component
public class LoginListener implements ApplicationListener<AbstractAuthenticationEvent> {

    @Autowired
    private LoginService loginService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(final AbstractAuthenticationEvent authenticationEvent) {

        // ignores to prevent duplicate logging with AuthenticationSuccessEvent
        if (!(authenticationEvent instanceof AuthenticationSuccessEvent))
            return;

        final Authentication authentication = authenticationEvent.getAuthentication();
        logUserLogin(authentication);
    }

    private void logUserLogin(final Authentication authentication) {

        var user = Optional.<User>empty();
        if (authentication.getPrincipal() instanceof User) {
            user = Optional.of((User) authentication.getPrincipal());
            final String auditMessage = "Login attempt with username: " + authentication.getName() +
                                        " Success: " + authentication.isAuthenticated() +
                                        " Request: " + makeReferrer(request) +
                                        " IP: " + request.getRemoteHost();

            loginService.addUserLogin(user.map(User::getId)
                                          .orElse(-1L),
                                      makeReferrer(request),
                                      request.getRemoteHost(),
                                      authentication.isAuthenticated());

            // TODO unit test.
            setLoginSessionTimeout(request);
            System.out.println(auditMessage);
        }
    }

    private static void setLoginSessionTimeout(final HttpServletRequest request) {
        final var loginSessionTimeout = 20;
        setTimeoutOnRequestSession(loginSessionTimeout, request);
    }

}