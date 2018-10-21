package plasma.xmf;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public final class ImportResolver {

    public static Optional<String> findXMF(String address) {
        address = address.trim();
        if (address.contains("\n"))
            return Optional.empty();
        //Let's see if there are hints
        if (address.startsWith("url:")) {
            try {
                return Optional.of(GET(address.substring(5).trim()));
            } catch (Exception e) {
                return Optional.empty();
            }
        } else if (address.startsWith("file:")) {
            try {
                return Optional.of(read(address.substring(6).trim()));
            } catch (IOException e) {
                return Optional.empty();
            }
        } else if (address.startsWith("classpath:")) {
            try {
                return Optional.of(readClasspath(address.substring(11)));
            } catch (IOException e) {
                return Optional.empty();
            }
        } else {
            //Ugh we just gotta try a bunch of stuff and hope that one works
            try {
                return Optional.of(read(address));
            } catch (Exception e) {
                try {
                    return Optional.of(readClasspath(address));
                } catch (Exception e2) {
                    try {
                        return Optional.of(GET(address));
                    } catch (Exception e3) {
                        return Optional.empty(); //Crap :(
                    }
                }
            }
        }
    }

    private static String readStream(InputStream stream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static String GET(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        return readStream(conn.getInputStream());
    }

    private static String read(String path) throws IOException {
        return readStream(new FileInputStream(path));
    }

    private static String readClasspath(String path) throws IOException {
        return readStream(ImportResolver.class.getResourceAsStream(path));
    }
}
