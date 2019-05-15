import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.slf4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class StartServer {

    public static void main(String[] args) throws IOException {

        FileInputStream refreshToken = new FileInputStream(
                "/Users/wdcunha/ESTG/2osemestre/Des Web/ServerFCM/src/main/resources/estg-firebase-notification-firebase-adminsdk-tuuym-440c711c60.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .build();

        FirebaseApp.initializeApp(options);


        Logger logger = LoggerFactory.getLogger("SampleLogger");

        //Logging the information
        logger.info("Hi This is my first SLF4J program");
    }

}
