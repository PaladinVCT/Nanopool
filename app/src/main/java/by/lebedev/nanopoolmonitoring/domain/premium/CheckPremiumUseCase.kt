package by.lebedev.nanopoolmonitoring.domain.premium

import android.content.SharedPreferences
import android.util.Log
import by.lebedev.nanopoolmonitoring.ui.BaseApplication
import by.lebedev.nanopoolmonitoring.utils.AppCoroutineDispatchers
import by.lebedev.nanopoolmonitoring.utils.UseCase
import by.lebedev.nanopoolmonitoring.utils.setPremium
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheckPremiumUseCase @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val sp: SharedPreferences,
    private val application: BaseApplication
) : UseCase<Unit, Unit>() {

    override val dispatcher = dispatchers.io

    override suspend fun doWork(params: Unit): Unit {

        application.billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    val queryParams = QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS)

                    CoroutineScope(Dispatchers.IO).launch {
                        val purchasesResult =
                            application.billingClient.queryPurchasesAsync(queryParams.build())
                        Log.e("!!!", purchasesResult.purchasesList.toString())
                        sp.setPremium(purchasesResult.purchasesList.isNotEmpty())
                    }

                }
            }
            override fun onBillingServiceDisconnected() {
            }
        })
    }
}