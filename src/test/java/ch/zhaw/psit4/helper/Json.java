package ch.zhaw.psit4.helper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

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

    /**
     * Serialize a given object to JSON.
     *
     * @param object object.
     * @return JSON String.
     * @throws Exception
     */
    public static String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Deserialize a JSON string.
     *
     * @param value JSON String
     * @param klass class type of result.
     * @param <T>   Type of result
     * @return Deserialized object.
     * @throws IOException
     */
    public static <T> T toObjectTypeSafe(String value, Class<T> klass) throws IOException {
        return objectMapper.readValue(value, klass);
    }
}
