import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class FCMServerStarter {

    private static String[] SCOPES = { "https://www.googleapis.com/auth/firebase.messaging"};

    private static String getAccessToken() throws IOException {
        GoogleCredential myGoogleCredential = GoogleCredential
                .fromStream(new FileInputStream("/Users/wdcunha/ESTG/2osemestre/Des Web/ServerFCM/src/main/resources/estg-firebase-notification-firebase-adminsdk-tuuym-440c711c60.json"))
                .createScoped(Arrays.asList(SCOPES));
        myGoogleCredential.refreshToken();
        return myGoogleCredential.getAccessToken();
    }

    public static void main(String[] args) throws IOException, JSONException {

        URL url = new URL("https://fcm.googleapis.com/v1/projects/estg-firebase-notification/messages:send");

        String token = getAccessToken();
        String tokenApp = "dtBg-Zk2bwE:APA91bEJmJ5ZvYao6ASVgBBs_cYDsLUGPae6GoogA6rsqaXoVZQZlVGciHhMF2GOVksWuItUdmjtxtbBy0gdpEbQcxFhCaHYX7OOWn_tkKqd5OeAkUHoQeDwxu3m3FH-N5Ks7dXTn1cD";
        String message = "Notificação FCM enviada pelo JavaServerFCM!";
        String body = "{ \"message\": {\"token\": \"dtBg-Zk2bwE:APA91bEJmJ5ZvYao6ASVgBBs_cYDsLUGPae6GoogA6rsqaXoVZQZlVGciHhMF2GOVksWuItUdmjtxtbBy0gdpEbQcxFhCaHYX7OOWn_tkKqd5OeAkUHoQeDwxu3m3FH-N5Ks7dXTn1cD\",\"notification\" : {\"title\" : \"A minha outra mensagem\",\"body\" : \"Notificação FCM enviada por postman que supõe ser o server!\"}}}";
        System.out.println("token: "+token);

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("message", message);
        jsonObject.put("token", tokenApp);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
//        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");

        OutputStream outputStream = httpURLConnection.getOutputStream();
//        outputStream.write(jsonObject.toString().getBytes("UTF-8"));
        outputStream.write(body.getBytes());
//        outputStream.close();

        outputStream.flush();

        StringBuilder sb = new StringBuilder();

        int httpResult = httpURLConnection.getResponseCode();

        if(httpResult == HttpURLConnection.HTTP_OK){
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(),"utf-8")
            );
            String line = null;

            while ((line = bufferedReader.readLine()) != null){
                sb.append(line + "\n");
            }
            System.out.println("Message sent succesfully!!!!");
            bufferedReader.close();
            httpURLConnection.disconnect();
            //return httpURLConnection.toString();

        } else {
            httpURLConnection.disconnect();
            //return httpURLConnection.getResponseMessage();
        }

        
        //Enviar objeto JSON com os dados que pretendemos mostrar no Android
    }
}
