package by.lebedev.nanopoolmonitoring.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.airbnb.mvrx.Mavericks
import com.android.billingclient.api.*
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication :Application(), Configuration.Provider {

    companion object{
        const val NOTIFY = "Nanopool notify"
    }

    private lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    lateinit var billingClient: BillingClient

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        Mavericks.initialize(this)
        createChannel()

        purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            if (!purchase.isAcknowledged) {
                                val acknowledgePurchaseParams =
                                    AcknowledgePurchaseParams.newBuilder()
                                        .setPurchaseToken(purchase.purchaseToken)
                                CoroutineScope(Dispatchers.IO).launch {
                                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                                }
                            }
                        }
                    }
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
//                    Log.e("!!!", "cancelled by user")
                } else {
//                    Log.e("!!!", "other errors")
                }
            }


        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Nanopool notification channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val description = "Channel for Nanopool workers notifications"
            val channel = NotificationChannel(NOTIFY, name, importance)
            channel.description = description
            channel.enableLights(true)
            channel.enableVibration(true)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

    }
}