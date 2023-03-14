/**********************************
*              @2023              *
**********************************/
package simplelogin;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

public final class RequestUtils {

    public static final String DID_NOT_EXIST_CODE = "DNE";

    public static String makeReferrer(final HttpServletRequest request)
    {
        if (Objects.isNull(request))
            throw new IllegalArgumentException("Valid request must be provided when making referrer.");

        return Optional.ofNullable(request.getHeader("referer"))
                       .orElse(DID_NOT_EXIST_CODE);
    }

    public static String makeUrl(final HttpServletRequest request)
    {
        if (Objects.isNull(request))
            throw new IllegalArgumentException("Valid request must be provided when making Url.");

        if (StringUtils.isBlank(request.getRequestURL()))
            return DID_NOT_EXIST_CODE;

        return request.getRequestURL()
                      .toString() +
                Optional.ofNullable(request.getQueryString())
                        .map(query -> "?" + query)
                        .orElse("");
    }

    public static boolean isValidHttpVerb(final String httpVerb) {
        if (StringUtils.isBlank(httpVerb))
            return false;

        final var httpVerbUppercase = httpVerb.toUpperCase();

        return (httpVerbUppercase.contains("GET") ||
                httpVerbUppercase.contains("PUT") ||
                httpVerbUppercase.contains("POST") ||
                httpVerbUppercase.contains("HEAD") ||
                httpVerbUppercase.contains("DELETE") ||
                httpVerbUppercase.contains("CONNECT") ||
                httpVerbUppercase.contains("OPTIONS") ||
                httpVerbUppercase.contains("TRACE") ||
                httpVerbUppercase.contains("PATCH"));
    }
}
