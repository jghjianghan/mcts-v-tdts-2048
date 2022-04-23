package util;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Helper class for HTTP REST requests.
 *
 * @author Jiang Han
 */
public class HttpHelper {

    public static void pingMe() {
        Dotenv dotenv = Dotenv.load();
        System.out.println(dotenv.get("API_SECRET"));
    }
}
