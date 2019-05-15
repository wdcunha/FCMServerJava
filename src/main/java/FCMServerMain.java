import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class FCMServerMain {

    private static String[] SCOPES = { "https://www.googleapis.com/auth/firebase.messaging"};

    private static HttpURLConnection connection;


    private static String getAccessToken() throws IOException {
        GoogleCredential myGoogleCredential = GoogleCredential
                .fromStream(new FileInputStream("/Users/wdcunha/ESTG/2osemestre/Des Web/ServerFCM/src/main/resources/estg-firebase-notification-firebase-adminsdk-tuuym-440c711c60.json"))
                .createScoped(Arrays.asList(SCOPES));
        myGoogleCredential.refreshToken();
        return myGoogleCredential.getAccessToken();
    }

    public static void main(String[] args) throws IOException, JSONException {

        try {

            URL url = new URL("https://fcm.googleapis.com/v1/projects/estg-firebase-notification/messages:send");

            String token = getAccessToken();
            String message = "Notificação FCM enviada por postman que supõe ser o server!";
            System.out.println("token: "+token);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("message", message);
            jsonObject.put("token", token);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Content-Type", "application/json; UTF-8");
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(jsonObject.toString().getBytes("UTF-8"));
            }

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            System.out.println("content.toString(): " + content.toString());

        } finally {

            connection.disconnect();
        }


    }
}
