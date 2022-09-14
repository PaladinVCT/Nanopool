package by.lebedev.nanopoolmonitoring.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.prices.CoinFiatPrices
import by.lebedev.nanopoolmonitoring.data.entities.workers.Worker
import by.lebedev.nanopoolmonitoring.domain.account.*
import by.lebedev.nanopoolmonitoring.ui.options.OptionsFragment
import by.lebedev.nanopoolmonitoring.utils.*
import com.afollestad.materialdialogs.utils.MDUtil.getStringArray
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [NanopoolWidgetConfigureActivity]
 */
@AndroidEntryPoint
class NanopoolWidget : AppWidgetProvider() {

    @Inject
    lateinit var tinyDb: TinyDB

    @Inject
    lateinit var sp: SharedPreferences

    @Inject
    lateinit var loadCurrentHashrateUseCase: LoadCurrentHashrateUseCase

    @Inject
    lateinit var loadBalanceUseCase: LoadBalanceUseCase

    @Inject
    lateinit var loadWorkersUseCase: LoadWorkersUseCase

    @Inject
    lateinit var loadCoinFiatPricesUseCase: LoadCoinFiatPricesUseCase


//    override fun onEnabled(context: Context?) {
//        super.onEnabled(context)
//
//        val intentSync = Intent(context, NanopoolWidget::class.java)
//        intentSync.action =
//            AppWidgetManager.ACTION_APPWIDGET_UPDATE //You need to specify the action for the intent. Right now that intent is doing nothing for there is no action to be broadcasted.
//
//
//        CoroutineScope(Dispatchers.IO).launch {
//            for (i in 1..10) {
//                delay(25000)
//                context?.sendBroadcast(intentSync)
//            }
//        }
//    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        val views = RemoteViews(context.packageName, R.layout.widget_nanopool)

        for (appWidgetId in appWidgetIds) {

            val intentSync = Intent(context, NanopoolWidget::class.java)
            intentSync.action =
                AppWidgetManager.ACTION_APPWIDGET_UPDATE //You need to specify the action for the intent. Right now that intent is doing nothing for there is no action to be broadcasted.
            val pendingSync = PendingIntent.getBroadcast(
                context,
                0,
                intentSync,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            //specify a proper flag for the intent. Or else the intent will become deleted.

            views.setOnClickPendingIntent(R.id.updateButton, pendingSync)

            appWidgetManager.updateAppWidget(appWidgetId, views)

            setAllWidgetData(
                context,
                appWidgetManager,
                appWidgetId,
                tinyDb
            )
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }


        // There may be multiple widgets active, so update all of them
//        for (appWidgetId in appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId, tinyDb)
//        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            tinyDb.deleteWidgetWallet(appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val appWidgetManager = AppWidgetManager.getInstance(context)

        val thisAppWidget =
            context?.packageName?.let { ComponentName(it, NanopoolWidget::class.java.name) }
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)


        if (context != null) {
            onUpdate(context, appWidgetManager, appWidgetIds)
        }

    }

    private fun setAllWidgetData(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        tinyDB: TinyDB
    ) {

        val nf = NumberFormat.getInstance()

        val wallet = tinyDB.loadWidgetWallet(appWidgetId)

        val views = RemoteViews(context.packageName, R.layout.widget_nanopool)

        nf.maximumFractionDigits = 3

        views.setTextColor(R.id.widgetCurrentStatus, Color.rgb(54, 137, 70))

        views.setTextViewText(
            R.id.widgetCurrentStatus, "Receiving data"
        )

        views.setTextViewText(
            R.id.widgetLastUpdated, "Updating..."
        )

        views.setTextViewText(
            R.id.widgetCurrentBalance, "Updating..."
        )

        appWidgetManager.updateAppWidget(appWidgetId, views)
        views.setTextViewText(
            R.id.widgetCurrentHashrate, "Updating..."
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)

        views.setTextViewText(
            R.id.widgetCurrentWorkers, "Updating..."
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)

        views.setTextViewText(
            R.id.widgetCurrentCoin,
            context.getString(R.string.widget_loading_coin)
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)

        if (isInternetAvailable(context) && wallet?.walletAddress?.isNotEmpty() == true) {

            loadCurrentHashrateUseCase.invoke(
                LoadCurrentHashrateUseCase.Params(
                    wallet.walletCoin.ticker,
                    wallet.walletAddress
                )
            ).trackError {
                views.setTextViewText(
                    R.id.widgetCurrentHashrate, "N/A"
                )
                views.setTextViewText(
                    R.id.widgetCurrentStatus, "Error"
                )
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
                .trackSuccess {
                    views.setTextViewText(
                        R.id.widgetCurrentHashrate,
                        context.getString(
                            R.string.hashrate_res,
                            it.data,
                            wallet.walletCoin.hashrate()
                        )
                    )
                    views.setTextViewText(
                        R.id.widgetCurrentStatus, "Ok"
                    )
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
                .launchIn(CoroutineScope(Dispatchers.IO))

            loadBalanceUseCase.invoke(
                LoadBalanceUseCase.Params(
                    wallet.walletCoin.ticker,
                    wallet.walletAddress
                )
            ).trackError {
                views.setTextViewText(
                    R.id.widgetCurrentBalance, "N/A"
                )
                views.setTextViewText(
                    R.id.widgetCurrentStatus, "Error"
                )
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
                .trackSuccess {
                    views.setTextViewText(
                        R.id.widgetCurrentBalance,
                        context.getString(
                            R.string.balance_res,
                            it.balance,
                            wallet.walletCoin.ticker.uppercase()
                        )
                    )
                    views.setTextViewText(
                        R.id.widgetCurrentStatus, "Ok"
                    )
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
                .launchIn(CoroutineScope(Dispatchers.IO))

            val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
            val currentTime = sdf.format(Date())
            views.setTextViewText(
                R.id.widgetLastUpdated,
                currentTime
            )
            appWidgetManager.updateAppWidget(appWidgetId, views)

            loadWorkersUseCase.invoke(
                LoadWorkersUseCase.Params(
                    wallet.walletCoin.ticker,
                    wallet.walletAddress
                )
            )
                .trackError {
                    views.setTextViewText(
                        R.id.widgetCurrentWorkers, "N/A"
                    )
                    views.setTextViewText(
                        R.id.widgetCurrentStatus, "Error"
                    )
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
                .trackSuccess {
                    views.setTextViewText(
                        R.id.widgetCurrentWorkers,
                        countAlive(it).toString()
                    )
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
                .launchIn(CoroutineScope(Dispatchers.IO))


            val selectedFiatIndex = sp.getString(
                OptionsFragment.SELECTED_FIAT,
                context.getStringArray(R.array.fiatCurrency)[0]
            )?.let {
                context.getStringArray(R.array.fiatCurrency).indexOf(it)
            } ?: 0

            loadCoinFiatPricesUseCase.invoke(
                LoadCoinFiatPricesUseCase.Params(
                    wallet.walletCoin.ticker
                )
            )
                .trackError {
                    views.setTextViewText(
                        R.id.widgetCurrentCoin, "N/A"
                    )
                    views.setTextViewText(
                        R.id.widgetCurrentStatus, "Error"
                    )
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
                .trackSuccess {
                    views.setTextViewText(
                        R.id.widgetCurrentCoin,
                        "${setSelectedFiatPrice(selectedFiatIndex, it)} ${context.getStringArray(R.array.fiatCurrency).get(selectedFiatIndex)}"
                    )
                    views.setTextViewText(
                        R.id.widgetCurrentStatus, "Ok"
                    )
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
                .launchIn(CoroutineScope(Dispatchers.IO))

            views.setImageViewResource(
                R.id.widgetCoinImage,
                wallet.walletCoin.ticker.toCoinImageRes()
            )
            appWidgetManager.updateAppWidget(appWidgetId, views)

            views.setViewVisibility(
                R.id.progressBar,
                View.GONE
            )
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun countAlive(workerList: List<Worker>): Int {
        var count = 0
        for (element in workerList) {
            if (element.hashrate != 0.0) {
                count++
            }
        }
        return count
    }

    private fun setSelectedFiatPrice(fiatIndex: Int, coinFiatPrices: CoinFiatPrices): String {
        return when (fiatIndex) {
            0 -> coinFiatPrices.priceUsd
            1 -> coinFiatPrices.priceRur
            2 -> coinFiatPrices.priceEur
            3 -> coinFiatPrices.priceGbp
            4 -> coinFiatPrices.priceCny
            else -> coinFiatPrices.priceUsd
        }
    }
}


fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    tinyDB: TinyDB
) {

}

