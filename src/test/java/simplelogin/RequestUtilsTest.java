/**********************************
*              @2023              *
**********************************/
package simplelogin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static simplelogin.RequestUtils.*;
import static simplelogin.TestUtils.createFakeServletRequest;

@ExtendWith(MockitoExtension.class)
public class RequestUtilsTest {

    @Test
    public void shouldMakeReferrerFromRequest() {
        final var headerMap = new HashMap<String, String>();
        final var referer = "http://somesite.com/atsomeresource.html";
        headerMap.put("referer", referer);
        final var request = createFakeServletRequest(null, headerMap);
        final var refererFromRequest = makeReferrer(request);
        assertThat(refererFromRequest).isEqualTo(referer);
    }

    @Test
    public void shouldReturnNonExistenceReferrerFromRequest() {
        final var headerMap = new HashMap<String, String>();
        headerMap.put("referer", null);
        final var request = createFakeServletRequest(null, headerMap);
        final var refererFromRequest = makeReferrer(request);
        assertThat(refererFromRequest).isEqualTo(DID_NOT_EXIST_CODE);
    }

    @Test
    public void shouldMakeUrlFromRequest() {
        final var headerMap = new HashMap<String, String>();
        final var requestUrl = "http://somesite.com/home.html";
        final var requestQuery = "action=save&error=true";
        headerMap.put("referer", null);
        final var request = createFakeServletRequest(null,
                                                     null,
                                                     requestUrl,
                                                     requestQuery,
                                                     null);
        final var refererFromRequest = makeUrl(request);
        assertThat(refererFromRequest).isEqualTo(requestUrl + "?" + requestQuery);
    }

    @Test
    public void shouldReturnNonExistenceUrlFromRequest() {
        final var refererFromRequest = makeUrl(createFakeServletRequest());
        assertThat(refererFromRequest).isEqualTo(DID_NOT_EXIST_CODE);
    }

    @Test()
    public void shouldThrowErrorOnInvalidRequestWhenMakingReferrer() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> makeReferrer(null),
                "shouldThrowErrorOnInvalidRequestWhenMakingReferrer"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid request must be provided when making referrer.");
    }

    @Test()
    public void shouldThrowErrorOnInvalidRequestWhenMakingUrl() {
        final var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> makeUrl(null),
                "shouldThrowErrorOnInvalidRequestWhenMakingUrl"
        );

        assertThat(thrown.getMessage()).isEqualTo("Valid request must be provided when making Url.");
    }

    @Test
    public void shouldReturnTrueForGetHttpVerb() {
        assertThat(isValidHttpVerb("get")).isTrue();
    }

    @Test
    public void shouldReturnTrueForPutHttpVerb() {
        assertThat(isValidHttpVerb("put")).isTrue();
    }

    @Test
    public void shouldReturnTrueForPostHttpVerb() {
        assertThat(isValidHttpVerb("post")).isTrue();
    }

    @Test
    public void shouldReturnTrueForHeadHttpVerb() {
        assertThat(isValidHttpVerb("head")).isTrue();
    }

    @Test
    public void shouldReturnTrueForDeleteHttpVerb() {
        assertThat(isValidHttpVerb("delete")).isTrue();
    }

    @Test
    public void shouldReturnTrueForConnectHttpVerb() {
        assertThat(isValidHttpVerb("connect")).isTrue();
    }

    @Test
    public void shouldReturnTrueForOptionsHttpVerb() {
        assertThat(isValidHttpVerb("options")).isTrue();
    }

    @Test
    public void shouldReturnTrueForTraceHttpVerb() {
        assertThat(isValidHttpVerb("trace")).isTrue();
    }

    @Test
    public void shouldReturnTrueForPatchHttpVerb() {
        assertThat(isValidHttpVerb("patch")).isTrue();
    }

    @Test()
    public void shouldReturnFalseWhenBlankHttpVerb() {
        assertThat(isValidHttpVerb("")).isFalse();
    }

    @Test()
    public void shouldApplyTimeoutToSessionRequest() {
        final var request = createFakeServletRequest();
        final var timeout = 42;
        setTimeoutOnRequestSession(timeout, request);
        verify(request.getSession(),
               times(1)).setMaxInactiveInterval(timeout);
    }
}
