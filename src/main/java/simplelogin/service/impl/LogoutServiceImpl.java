/**********************************
*              @2023              *
**********************************/
package simplelogin.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplelogin.entity.Logout;
import simplelogin.repository.LogoutRepository;
import simplelogin.service.LogoutService;

import java.util.Date;


@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private LogoutRepository logoutRepository;

    @Override
    public void addUserLogout(final long id,
                              final String uri,
                              final String ip) {
        // Validate
        LogoutService.super.addUserLogout(id, uri, ip);
        // Save
        final var logout = new Logout();
        logout.setUserId(id);
        logout.setUri(uri);
        logout.setIp(ip);
        logout.setLogoutDate(new Date());
        logoutRepository.save(logout);
    }

}
