package by.lebedev.nanopoolmonitoring.ui.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.databinding.NanopoolWidgetConfigureBinding
import by.lebedev.nanopoolmonitoring.ui.widget.configure.ConfigureWidgetFragment
import by.lebedev.nanopoolmonitoring.utils.TinyDB
import by.lebedev.nanopoolmonitoring.utils.saveWidgetWallet
import by.lebedev.nanopoolmonitoring.utils.saveWidgetWalletAddress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The configuration screen for the [NanopoolWidget] AppWidget.
 */

@AndroidEntryPoint
class NanopoolWidgetConfigureActivity : AppCompatActivity(),ConfigureWidgetFragment.WidgetConfigureCallback {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    @Inject
    lateinit var tinyDb: TinyDB

    private lateinit var binding: NanopoolWidgetConfigureBinding

    public override fun onCreate(icicle: Bundle?) {
        installSplashScreen()
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        binding = NanopoolWidgetConfigureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
    }

    private fun startWidget(){
        val context = this@NanopoolWidgetConfigureActivity

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
//        updateAppWidget(context, appWidgetManager, appWidgetId,tinyDb)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)

        val intentSync = Intent(context, NanopoolWidget::class.java)
        intentSync.action =
            AppWidgetManager.ACTION_APPWIDGET_UPDATE //You need to specify the action for the intent. Right now that intent is doing nothing for there is no action to be broadcasted.

        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..10) {
                delay(i*10000L)
                context.sendBroadcast(intentSync)
            }
        }

        finish()
    }

    override fun onWalletSelected(wallet: Wallet) {
        tinyDb.saveWidgetWallet(appWidgetId,wallet)
        startWidget()
    }


}