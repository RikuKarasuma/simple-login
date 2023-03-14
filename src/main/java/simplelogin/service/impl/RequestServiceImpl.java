/**********************************
*              @2023              *
**********************************/
package simplelogin.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simplelogin.entity.Request;
import simplelogin.repository.RequestRepository;
import simplelogin.service.RequestService;

import java.util.Date;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public void addUserRequest(final long id,
                               final String uri,
                               final String ip,
                               final String method) {
        // Validate
        RequestService.super.addUserRequest(id, uri, ip, method);
        // Save
        final var request = new Request();
        request.setUserId(id);
        request.setUri(uri);
        request.setIp(ip);
        request.setMethod(method);
        request.setRequestDate(new Date());
        requestRepository.save(request);
    }
}
