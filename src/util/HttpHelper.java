package util;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Helper class for HTTP REST requests.
 *
 * @author Jiang Han
 */
public class HttpHelper {

    public static void main(String[] args) throws IOException {
        pingMe();
    }
    
    public static void pingMe() throws MalformedURLException, IOException {
        Dotenv dotenv = Dotenv.load();
        
        URL url = new URL(dotenv.get("SERVER_URL"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.setDoOutput(true);
        
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = dotenv.get("API_SECRET").getBytes("utf-8");
            os.write(input, 0, input.length);			
        }
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
              StringBuilder response = new StringBuilder();
              String responseLine;
              while ((responseLine = br.readLine()) != null) {
                  response.append(responseLine.trim());
              }
          }
    }
}
