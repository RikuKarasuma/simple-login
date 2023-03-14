/**********************************
*              @2023              *
**********************************/
package simplelogin.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserDetailService extends UserDetailsService {

    void createNewUser(HttpServletRequest request,
                       HttpServletResponse response) throws IOException;
}
