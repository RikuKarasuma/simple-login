/**********************************
*              @2023              *
**********************************/
package simplelogin.service;

import org.apache.commons.lang3.StringUtils;

public interface LogoutService {
    default void addUserLogout(final long id,
                               final String uri,
                               final String ip) {
        if (id < 1)
            throw new IllegalArgumentException("Valid User Id must be provided when adding user logout.");
        else if (StringUtils.isBlank(uri))
            throw new IllegalArgumentException("Valid Referer must be provided when adding user logout.");
        else if (StringUtils.isBlank(ip))
            throw new IllegalArgumentException("Valid User Ip must be provided when adding user logout.");
    }
}
