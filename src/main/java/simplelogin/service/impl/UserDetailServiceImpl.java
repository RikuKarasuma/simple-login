/**********************************
*              @2023              *
**********************************/
package simplelogin.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import simplelogin.entity.User;
import simplelogin.repository.UserRepository;
import simplelogin.service.UserDetailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Service
@RestController
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username))
            return null;

        final var userOpt = userRepository.getUser(username);
        if (userOpt.isPresent()) {
            final var user = userOpt.get();
            final Authentication authentication = new UsernamePasswordAuthenticationToken(user,
                                                                                          user.getPassword(),
                                                                                          user.getAuthorities());

            SecurityContextHolder.getContext()
                                 .setAuthentication(authentication);
            return user;
        }

        return null;
    }

    @Override
    @PostMapping("createUser")
    public void createNewUser(final HttpServletRequest request,
                              final HttpServletResponse response) throws IOException {

        if (Objects.isNull(request))
            throw new IllegalArgumentException("Valid request must be provided when creating a user.");
        else if (Objects.isNull(response))
            throw new IllegalArgumentException("Valid response must be provided when creating a user.");

        final var username = request.getParameter("username");
        final var password = request.getParameter("password");

        if (StringUtils.isBlank(username))
            throw new IllegalArgumentException("Username must be provided when creating a user.");
        else if (StringUtils.isBlank(password))
            throw new IllegalArgumentException("Password must be provided when creating a user.");

        final var newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(newUser);
        response.sendRedirect("/Index.html");
    }
}
