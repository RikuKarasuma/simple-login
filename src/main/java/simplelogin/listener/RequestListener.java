/**********************************
*              @2023              *
**********************************/
package simplelogin.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import simplelogin.entity.User;
import simplelogin.service.RequestService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static simplelogin.RequestUtils.makeUrl;

@Order(value = Ordered.LOWEST_PRECEDENCE)
@Component
@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
public class RequestListener extends OncePerRequestFilter {

    @Autowired
    private RequestService requestService;

    @Override
    public void doFilterInternal(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final FilterChain filterChain) throws ServletException, IOException {

        final var authenticationOpt = Optional.ofNullable(SecurityContextHolder.getContext()
                                                                               .getAuthentication());

        var auditMessage = "Request made: " + makeUrl(request) +
                           " IP: " +request.getRemoteHost() +
                           " ACTION: " + request.getMethod();

        var user = Optional.<User>empty();
        if (authenticationOpt.isPresent()) {
            var authentication = authenticationOpt.get();
            auditMessage = " Request made with username: " + authentication.getName() +
                           " Authenticated: " + authentication.isAuthenticated() +
                           " Request: " + makeUrl(request) +
                           " IP: " + request.getRemoteHost() +
                           " ACTION: " + request.getMethod();

            if (authentication.getPrincipal() instanceof User) {
                user = Optional.of((User) authentication.getPrincipal());
            }
        }

        requestService.addUserRequest(user.map(User::getId)
                                          .orElse(-1L),
                                      makeUrl(request),
                                      request.getRemoteHost(),
                                      request.getMethod());

        System.out.println(auditMessage);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return true;
    }
}
