/**********************************
*              @2023              *
**********************************/
package simplelogin.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplelogin.entity.Login;
import simplelogin.repository.LoginRepository;
import simplelogin.service.LoginService;

import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public void addUserLogin(final long id,
                             final String uri,
                             final String ip,
                             final boolean success) {
        // Validate
        LoginService.super.addUserLogin(id, uri, ip, success);
        // Save
        final var login = new Login();
        login.setUserId(id);
        login.setLoginDate(new Date());
        login.setSuccess(success);
        login.setUri(uri);
        login.setIp(ip);
        loginRepository.save(login);
    }
}
