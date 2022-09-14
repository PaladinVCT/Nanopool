package by.lebedev.nanopoolmonitoring.ui.fcm

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
    }

    override fun handleIntent(p0: Intent?) {
        super.handleIntent(p0)
    }
}