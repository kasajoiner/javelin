package javelin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface ResourceTestHelper {

    default String readResource(String name) {
        try {
            URI responseUri = getClass().getClassLoader().getResource(name).toURI();
            return Files.readString(Paths.get(responseUri));
        } catch (Exception e) {
            throw new IllegalStateException("Can't read resource", e);
        }
    }

    default InputStream readResourceAsStream(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    default File readFile(String name) {
        try {
            return new File(getClass().getClassLoader().getResource(name).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    default byte[] readResourceAsBytes(String name) throws IOException {
        return readResourceAsStream(name).readAllBytes();
    }

}
