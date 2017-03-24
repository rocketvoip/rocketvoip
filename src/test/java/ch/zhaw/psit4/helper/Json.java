package ch.zhaw.psit4.helper;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON helper for tests.
 *
 * @author Rafael Ostertag
 */
final public class Json {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Json() {
        // intentionally empty
    }

    public static String toJSon(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
}
