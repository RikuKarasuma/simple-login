/**********************************
*              @2023              *
**********************************/
package simplelogin.service;

import org.apache.commons.lang3.StringUtils;

import static simplelogin.RequestUtils.isValidHttpVerb;

public interface RequestService {

    default void addUserRequest(long id,
                                String uri,
                                String ip,
                                String method) {
        if (id == 0 || id < -1)
            throw new IllegalArgumentException("Valid User Id must be provided when adding user request.");
        else if (StringUtils.isBlank(uri))
            throw new IllegalArgumentException("Valid Url must be provided when adding user request.");
        else if (StringUtils.isBlank(ip))
            throw new IllegalArgumentException("Valid User Ip must be provided when adding user request.");
        else if (StringUtils.isBlank(method))
            throw new IllegalArgumentException("Valid HTTP Method must be provided when adding user request.");
        else if (!isValidHttpVerb(method))
            throw new IllegalArgumentException("Valid HTTP Method must be provided when adding user request.");
    }
}
