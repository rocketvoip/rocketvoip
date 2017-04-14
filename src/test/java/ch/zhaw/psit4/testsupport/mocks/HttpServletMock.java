package ch.zhaw.psit4.testsupport.mocks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;

/**
 * @author Rafael Ostertag
 */
public final class HttpServletMock {
    private HttpServletMock() {
        // intentionally empty
    }

    public static HttpServletResponse mockHttpServletResponse() {
        return mock(HttpServletResponse.class);
    }

    public static HttpServletRequest mockHttpServletRequest() {
        return mock(HttpServletRequest.class);
    }
}
