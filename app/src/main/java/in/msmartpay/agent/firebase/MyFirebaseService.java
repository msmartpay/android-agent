package in.msmartpay.agent.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.Util;

public class MyFirebaseService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        // TODO Step 3.5 check messages for data
        // Check if message contains a data payload.
        // message.getData()
        // TODO Step 3.6 check messages for notification and call sendNotification
        // Check if message contains a notification payload.
        message.getNotification();
        if ( message.getNotification()!=null){
            sendNotification(message);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Util.SavePrefData(getApplicationContext(), Keys.FCM_TOKEN,token);
    }

    private void sendNotification(RemoteMessage message) {

    }
}
