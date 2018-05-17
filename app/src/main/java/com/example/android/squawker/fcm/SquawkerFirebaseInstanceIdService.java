package com.example.android.squawker.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class SquawkerFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = SquawkerFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToService(token);
    }

    private void sendRegistrationToService(String token) {
    }
}
