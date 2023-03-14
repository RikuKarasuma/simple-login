/**********************************
*              @2023              *
**********************************/
package simplelogin.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import simplelogin.entity.User;
import simplelogin.service.LogoutService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static simplelogin.RequestUtils.makeReferrer;

@Component
public class LogoutListener implements LogoutHandler {

    @Autowired
    private LogoutService logoutService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        if (Objects.isNull(request))
            throw new IllegalArgumentException("Logout can't be logged without HTTP request.");
        else if (Objects.isNull(response))
            throw new IllegalArgumentException("Logout can't be logged without HTTP response.");
        else if (Objects.isNull(authentication))
            throw new IllegalArgumentException("Logout can't be logged without Authentication.");

        final String auditMessage = " Logout attempt with username: " + authentication.getName() +
                                    " Success: " + authentication.isAuthenticated() +
                                    " Request: " + makeReferrer(request) +
                                    " IP: " + request.getRemoteHost();

        System.out.println(auditMessage);
        var user = Optional.<User>empty();
        if (authentication.getPrincipal() instanceof User) {
            user = Optional.of((User) authentication.getPrincipal());
        }

        logoutService.addUserLogout(user.map(User::getId)
                                        .orElse(-1L),
                                    makeReferrer(request),
                                    request.getRemoteHost());;

        try {
            response.sendRedirect("/Login.html");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
