package by.lebedev.nanopoolmonitoring.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.KeyListener
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.res.use
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import androidx.databinding.adapters.ListenerUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import by.lebedev.nanopoolmonitoring.BuildConfig
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.chart.ChartData
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.ui.BaseApplication
import by.lebedev.nanopoolmonitoring.ui.NanopoolFragment
import by.lebedev.nanopoolmonitoring.ui.mainscreen.MainScreenActivity
import by.lebedev.nanopoolmonitoring.ui.options.OptionsFragment
import by.lebedev.nanopoolmonitoring.ui.options.OptionsFragment.Companion.ADS_INTERNAL
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.*
import com.afollestad.materialdialogs.MaterialDialog
import com.airbnb.mvrx.Mavericks
import com.android.billingclient.api.*
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.HttpException
import java.text.NumberFormat
import java.util.*


fun PaddingView.toPxType(context: Context): PaddingView = when (paddingType) {
    PaddingView.PaddingType.DP -> mapBy { context.dpToPx(it) }
    PaddingView.PaddingType.RESOURCE -> mapBy { context.resToPx(it) }
    PaddingView.PaddingType.PX -> this
}

private inline fun PaddingView.mapBy(mapper: (Int) -> (Int)): PaddingView {
    return PaddingView.px(
        if (left != PaddingView.NO_VALUE_SET) mapper(left) else PaddingView.NO_VALUE_SET,
        if (top != PaddingView.NO_VALUE_SET) mapper(top) else PaddingView.NO_VALUE_SET,
        if (right != PaddingView.NO_VALUE_SET) mapper(right) else PaddingView.NO_VALUE_SET,
        if (bottom != PaddingView.NO_VALUE_SET) mapper(bottom) else PaddingView.NO_VALUE_SET
    )
}

@Px
fun Context.dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Int {
    return TypedValue
        .applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
            resources.displayMetrics
        ).toInt()
}

@Px
fun Context.resToPx(@DimenRes itemSpacingRes: Int): Int {
    return if (itemSpacingRes != 0) resources.getDimensionPixelOffset(itemSpacingRes) else 0
}

@SuppressLint("Recycle") // Shortsighted Lint doesn't know about contact of  'use' method
@ColorInt
fun Context.getThemeColor(@ColorRes colorRes: Int): Int {
    return obtainStyledAttributes(intArrayOf(colorRes)).use {
        it.getColor(0, Color.MAGENTA)
    }
}

fun TextView.setTextIfNew(charSequence: CharSequence?): Boolean =
    if (isTextDifferent(
            charSequence,
            text
        )
    ) {
        text = charSequence
        true
    } else false // Previous text is the same


fun TextInputLayout.updateError(message: CharSequence?) {
    if (isTextDifferent(error, message)) {
        error = message
    }
}

interface StorableValueEnum<T> {
    val value: T
}

fun TextInputLayout.updateHelperText(text: CharSequence?) {
    if (isTextDifferent(helperText, text)) {
        helperText = text
    }
}

fun TextInputLayout.updateHint(hintMessage: CharSequence?) {
    if (isTextDifferent(hint, hintMessage)) {
        hint = hintMessage
    }
}

fun EditText.updateInputType(type: Int) {
    if (inputType != type) {
        inputType = type
    }
}

fun EditText.updateKeyListener(listener: KeyListener) {
    if (keyListener != listener) {
        keyListener = listener
    }
}

fun isTextDifferent(str1: CharSequence?, str2: CharSequence?): Boolean {
    if (str1 === str2) {
        return false
    }
    if (str1 == null || str2 == null) {
        return true
    }
    val length = str1.length
    if (length != str2.length) {
        return true
    }

    if (str1 is Spanned) {
        return str1 != str2
    }

    for (i in 0 until length) {
        if (str1[i] != str2[i]) {
            return true
        }
    }
    return false
}

fun View.setViewPadding(padding: PaddingView?) {

    if (padding == null) {
        setPadding(0)
    } else {
        val paddingPx = padding.toPxType(context)
        setPadding(
            paddingPx.left,
            paddingPx.top,
            paddingPx.right,
            paddingPx.bottom
        )
    }
}

fun EditText.setUpInput(tag: String, input: Input?, callback: OnValueChange<Input>?) {
    setOnInputChange(callback)
    setInputAndCursor(tag, input)
}

fun EditText.setOnInputChange(callback: OnValueChange<Input>?) {
    // get previous TextWatcher for this EditText
    val watcher: TextWatcher? =
        ListenerUtil.getListener<TextWatcher>(
            this,
            R.id.tag_text_dynamic_watcher
        )
    if (watcher != null && watcher is DynamicRegularInputWatcher) {
        // watcher exist and is DynamicRegularInputWatcher
        // NOTE: is check cast NOT for DynamicInputWatcher
        watcher.onValueChanged = callback
    } else {
        // if watcher is null or watcher is not class of Regular input Watcher
        //remove previous
        removeTextChangedListener(watcher)
        // create new RegularInputWatcher
        val newWatcher =
            DynamicRegularInputWatcher(callback)
        addTextChangedListener(newWatcher)
        ListenerUtil.trackListener(
            this, newWatcher,
            R.id.tag_text_dynamic_watcher
        )
    }
}

fun EditText.setInputAndCursor(tag: String, input: Input?) {
    // Handle null case
    if (input == null) {
        //reset tag
        inputTag = null
        if (isTextDifferent(null, this.text)) {
            // reset text if needed
            text = null
        }
        return
    }

    if (inputTag != tag) {
        inputTag = tag
        if (isTextDifferent(input.text, this.text)) {
            setTextSilent(input.text)
            // If the text changed then we move the cursor to the end of the new text.
            // This allows us to fill in text programmatically if needed, like a search suggestion,
            // but if the user is typing and the view is rebound we won't lose their cursor position.
            setSelection(length())
        }
    } else if (isTextDifferent(input.text, this.text) && input.isFromState) {
        setTextSilent(input.text)
        setSelection(length())
    }
}

fun EditText.setUpMaskedInput(
    tag: String,
    mask: String,
    input: Input?,
    callback: OnValueChange<Input>?
) {
    setOnMaskedInputChange(mask, callback)
    setInputAndCursor(tag, input)
}

fun EditText.setOnMaskedInputChange(mask: String, callback: OnValueChange<Input>?) {
    val watcher: TextWatcher? =
        ListenerUtil.getListener<TextWatcher>(
            this,
            R.id.tag_text_dynamic_watcher
        )
    if (watcher != null && watcher is DynamicMaskedInputWatcher) {
        watcher.onValueChanged = callback
    } else {
        removeTextChangedListener(watcher)
        val newWatcher =
            DynamicMaskedInputWatcher(
                primaryFormat = mask,
                onValueChanged = callback,
                editText = this
            )
        addTextChangedListener(newWatcher)
        ListenerUtil.trackListener(
            this, newWatcher,
            R.id.tag_text_dynamic_watcher
        )
    }
}

/** Override padding in a vertical plane with new [padding], without change of paddingType. */
fun PaddingView.vertical(padding: Int): PaddingView =
    copy(top = padding, bottom = padding)


/** Override padding in a horizontal plane with new [padding], without change of paddingType. */
fun PaddingView.horizontal(padding: Int): PaddingView =
    copy(left = padding, right = padding)

fun OkHttpClient.Builder.setupTimeout(timeouts: Timeouts): OkHttpClient.Builder {
    connectTimeout(timeouts.connect, timeouts.timeUnit)
    readTimeout(timeouts.read, timeouts.timeUnit)
    writeTimeout(timeouts.write, timeouts.timeUnit)
    return this
}

val HttpException.errorResponse: String?
    get() = response()?.errorBody()?.string()

val HttpException.errorMessage: String
    get() {
        val msg = response()!!.errorBody()?.string()
        return if (msg.isNullOrEmpty()) {
            message()
        } else {
            msg
        } ?: "unknown error"
    }

fun CoinTicker.toCoin(): Coin = CoinBearer.coinList.first {
    it.ticker == this.value
}

fun String.textToCoinTicker(): CoinTicker =
    when (this) {
        "ethw" -> CoinTicker.ETHEREUM_POW
        "eth" -> CoinTicker.ETHEREUM
        "etc" -> CoinTicker.ETHEREUM_CLASSIC
        "zec" -> CoinTicker.ZCASH
        "xmr" -> CoinTicker.MONERO
        "rvn" -> CoinTicker.RAVEN
        "cfx" -> CoinTicker.CONFLUX
        "ergo" -> CoinTicker.ERGO
        else -> CoinTicker.ETHEREUM
    }

fun String.toCoinImageRes(): Int =
    when (this) {
        "ethw" -> R.drawable.ethw_logo
        "eth" -> R.drawable.eth_logo
        "etc" -> R.drawable.etc_logo
        "zec" -> R.drawable.zec_logo
        "xmr" -> R.drawable.xmr_logo
        "rvn" -> R.drawable.rvn_logo
        "cfx" -> R.drawable.cfx_logo
        "ergo" -> R.drawable.erg_logo
        else -> R.drawable.eth_logo
    }

fun Coin.hashrate(): String {
    return when (this.ticker.textToCoinTicker()) {
        CoinTicker.ETHEREUM,CoinTicker.ETHEREUM_POW, CoinTicker.RAVEN -> "Mh/s"
        CoinTicker.ETHEREUM_CLASSIC -> "Mh/s"
        CoinTicker.ZCASH -> "Sol/s"
        CoinTicker.MONERO -> "H/s"
        CoinTicker.CONFLUX -> "MH/s"
        CoinTicker.ERGO -> "MH/s"
        else -> "Mh/s"
    }
}

fun Coin.hashrateSmall(): String {
    return when (this.ticker.textToCoinTicker()) {
        CoinTicker.ETHEREUM,CoinTicker.ETHEREUM_POW, CoinTicker.RAVEN -> "Kh/s"
        CoinTicker.ETHEREUM_CLASSIC -> "Kh/s"
        CoinTicker.ZCASH -> "Sol/s"
        CoinTicker.MONERO -> "H/s"
        CoinTicker.CONFLUX -> "KH/s"
        CoinTicker.ERGO -> "KH/s"
        else -> "Mh/s"
    }
}

fun Coin.hashrateBig(): String {
    return when (this.ticker.textToCoinTicker()) {
        CoinTicker.ETHEREUM,CoinTicker.ETHEREUM_POW, CoinTicker.RAVEN -> "Gh/s"
        CoinTicker.ETHEREUM_CLASSIC -> "Gh/s"
        CoinTicker.ZCASH -> "KSol/s"
        CoinTicker.MONERO -> "KH/s"
        CoinTicker.CONFLUX -> "GH/s"
        CoinTicker.ERGO -> "GH/s"
        else -> "Mh/s"
    }
}

fun Coin.geckoCoinName(): String {
    return when (this.ticker.textToCoinTicker()) {
        CoinTicker.ETHEREUM_POW -> "ethereum-pow-iou"
        CoinTicker.ETHEREUM -> "ethereum"
        CoinTicker.ETHEREUM_CLASSIC -> "ethereum-classic"
        CoinTicker.ZCASH -> "zcash"
        CoinTicker.MONERO -> "monero"
        CoinTicker.RAVEN -> "raven"
        CoinTicker.CONFLUX -> "conflux"
        CoinTicker.ERGO -> "ergo"
        else -> "ethereum"
    }
}

inline fun <reified T : Any> Fragment.lazyArg(key: String): Lazy<T> = lazyFast {
    val args = arguments ?: throw IllegalStateException("Missing arguments!")
    @Suppress("UNCHECKED_CAST")
    args.get(key) as T
}

inline fun <reified T> lazyFast(crossinline operation: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) {
        operation()
    }

fun Fragment.handleDefaultError(throwable: Throwable) {
    if (throwable is ThrowableResult) {

        longToast(throwable.message ?: throwable.type.toLocalizedString(requireContext()))

        if (throwable.type == ErrorType.Unauthorized) {
            requireActivity().restart()
        }
    } else if (BuildConfig.DEBUG) {
        longToast(throwable.message ?: return)
    }
}

fun ErrorType?.toLocalizedString(context: Context): String {
    val res = when (this) {
        ErrorType.IO -> R.string.error_io
        ErrorType.Network -> R.string.error_network
        ErrorType.NetworkConnection -> R.string.error_connection
        ErrorType.JsonParsing -> R.string.error_mapping
        ErrorType.Timeout -> R.string.error_timeout
        ErrorType.Unauthorized -> R.string.error_unauthorized
        ErrorType.Unclassified -> R.string.error_unclassified
        null -> null
    }
    return res?.let { context.getString(it) }.orEmpty()
}


fun Activity.restart() {
    finish()
    startActivity(Intent(this, this::class.java))
}

fun <T : Parcelable> T.toBundleMvRx(): Bundle = bundleOf(Mavericks.KEY_ARG to this)

fun Double.round(digitsAfterDot: Int): Double {
    val nf = NumberFormat.getInstance(Locale.getDefault())
    return nf.parse(String.format("%.${digitsAfterDot}f", this))?.toDouble() ?: 0.0
}

fun Double.roundBigDecimalString(digitsAfterDot: Int): String {
    val nf = NumberFormat.getInstance(Locale.getDefault())
    return nf.parse(String.format("%.${digitsAfterDot}f", this))?.toDouble()?.toBigDecimal()
        .toString()
}

fun Double.roundPlusHashrate(digitsAfterDot: Int): String {
    return String.format("%.${digitsAfterDot}f", this)
        .plus(" ${SessionBearer.wallet?.walletCoin?.hashrate()}")
}

fun String.capitalizeFirstLetter(): String {
    val sb = StringBuilder(this)
    return sb.apply {
        setCharAt(0, sb[0].uppercaseChar())
    }.toString()
}

fun String.percent(): String {
    return this.plus("%")
}

fun SharedPreferences.logout() {
    SessionBearer.resetWallet()
    edit().putBoolean(OptionsFragment.IS_LOGGED_OUT, true).apply()
}

fun SharedPreferences.login() {
    edit().putBoolean(OptionsFragment.IS_LOGGED_OUT, false).apply()
}

fun SharedPreferences.setPremium(flag: Boolean) {
    edit().putBoolean(OptionsFragment.IS_PREMIUM, flag).apply()
}

fun SharedPreferences.checkPremium(): Boolean {
    return getBoolean(OptionsFragment.IS_PREMIUM, false)
}

fun SharedPreferences.setNoAdsInternal() {
    edit().putLong(ADS_INTERNAL, Calendar.getInstance().timeInMillis.plus(BuildConfig.NO_ADS_AFTER_WATCHING_AD)).apply()
}

fun SharedPreferences.isAdsInterval(): Boolean {
    val nextAdTime = getLong(ADS_INTERNAL, 0L)
    return Calendar.getInstance().timeInMillis > nextAdTime
}

fun SharedPreferences.saveWidgetWalletAddress(widgetId: Int, walletAddress: String) {
    edit().putString("${widgetId}_WIDGET_WALLET_ADDRESS", walletAddress).apply()
}

fun TinyDB.saveWidgetWallet(widgetId: Int, wallet: Wallet) {
    putObject("${widgetId}_WIDGET_WALLET_ADDRESS", wallet)
}

fun TinyDB.loadWidgetWallet(widgetId: Int): Wallet? {
    return getObject("${widgetId}_WIDGET_WALLET_ADDRESS", Wallet::class.java)
}

fun TinyDB.deleteWidgetWallet(widgetId: Int) {
    remove("${widgetId}_WIDGET_WALLET_ADDRESS")
}

fun SharedPreferences.loadWidgetWalletAddress(widgetId: Int): String {
    return getString("${widgetId}_WIDGET_WALLET_ADDRESS", "").orEmpty()
}

fun ChartData.generateHashrate(): ChartData {

    return ChartData(
        status = this.status,
        data = mutableListOf<ChartData.Data>().apply {
            this@generateHashrate.data.forEach {
                add(
                    ChartData.Data(
                        date = it.date,
                        hashrate = it.shares.times(coinHashrateModifier()).toLong()
                    )
                )
            }
        }
    )
}

fun Fragment.getPremium() {

    (requireActivity().application as BaseApplication).billingClient.startConnection(object :
        BillingClientStateListener {
        override fun onBillingSetupFinished(billingResult: BillingResult) {
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                val productList = listOf(
                    Product.newBuilder()
                        .setProductId("nanopoolmonitoring")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )

                val params = QueryProductDetailsParams.newBuilder()
                params.setProductList(productList)

                CoroutineScope(Dispatchers.IO).launch {
                    val productDetailsResult =
                        (requireActivity().application as BaseApplication).billingClient.queryProductDetails(
                            params.build()
                        )
                    withContext(Dispatchers.Main) {
//                        Log.e("!!!", productDetailsResult.productDetailsList.toString())

                        if (productDetailsResult.productDetailsList?.isNotEmpty() == true) {
                            val productDetails = productDetailsResult.productDetailsList!![0]

                            val productDetailsParamsList = listOf(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(productDetails)
                                    .setOfferToken(productDetails.subscriptionOfferDetails?.get(0)?.offerToken.orEmpty())
                                    .build()
                            )
                            val billingFlowParams = BillingFlowParams.newBuilder()
                                .setProductDetailsParamsList(productDetailsParamsList)
                                .build()

                            // Launch the billing flow
                            (requireActivity().application as BaseApplication).billingClient.launchBillingFlow(
                                requireActivity(),
                                billingFlowParams
                            )
                        }
                    }
                }
            }
        }

        override fun onBillingServiceDisconnected() {
        }
    })
}

fun Activity.getPremium() {

    (application as BaseApplication).billingClient.startConnection(object :
        BillingClientStateListener {
        override fun onBillingSetupFinished(billingResult: BillingResult) {
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                val productList = listOf(
                    Product.newBuilder()
                        .setProductId("nanopoolmonitoring")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )

                val params = QueryProductDetailsParams.newBuilder()
                params.setProductList(productList)

                CoroutineScope(Dispatchers.IO).launch {
                    val productDetailsResult =
                        (application as BaseApplication).billingClient.queryProductDetails(
                            params.build()
                        )
                    withContext(Dispatchers.Main) {
//                        Log.e("!!!", productDetailsResult.productDetailsList.toString())

                        if (productDetailsResult.productDetailsList?.isNotEmpty() == true) {
                            val productDetails = productDetailsResult.productDetailsList!![0]

                            val productDetailsParamsList = listOf(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(productDetails)
                                    .setOfferToken(productDetails.subscriptionOfferDetails?.get(0)?.offerToken.orEmpty())
                                    .build()
                            )
                            val billingFlowParams = BillingFlowParams.newBuilder()
                                .setProductDetailsParamsList(productDetailsParamsList)
                                .build()

                            // Launch the billing flow
                            (application as BaseApplication).billingClient.launchBillingFlow(
                                this@getPremium,
                                billingFlowParams
                            )
                        }
                    }
                }
            }
        }

        override fun onBillingServiceDisconnected() {
        }
    })
}

fun Fragment.rateApp() {
    val manager = ReviewManagerFactory.create(requireContext())
    val request = manager.requestReviewFlow()
    request.addOnCompleteListener { request ->
        if (request.isSuccessful) {
            // We got the ReviewInfo object
            val reviewInfo = request.result
            val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
            flow.addOnCompleteListener { _ ->
                // The flow has finished. The API does not indicate whether the user
                // reviewed or not, or even whether the review dialog was shown. Thus, no
                // matter the result, we continue our app flow.
            }
        } else {
            this.view?.let {
                Snackbar.make(it, "Error reviewing app", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}


fun Activity.showLimitReachedDialog(premiumClickedCallback: (MaterialDialog) -> Unit,adWatchedCallback: (MaterialDialog) -> Unit) {
    val dialog = MaterialDialog(this)
    val dialogView = LayoutInflater.from(this)
        .inflate(R.layout.fragment_limits_dialog, null, false)

    dialogView.findViewById<TextView>(R.id.subscribe_btn).setOnClickListener {
       this.getPremium()
        premiumClickedCallback(dialog)
    }
    dialogView.findViewById<TextView>(R.id.watch_ad_btn).setOnClickListener { view ->
        (this as MainScreenActivity).showRewardedAd {
            if (it) {
                adWatchedCallback(dialog)
            } else {
                Snackbar.make(view, "The rewarded ad wasn't ready yet.", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }
    dialog.apply {
        setContentView(dialogView)
        cancelable(!cancelable)
    }.show()
}

fun Fragment.handleDefaultNavigation(
    navController: NavController,
    navEvent: BaseNavEvent,
    processScope: CoroutineScope? = null
): Unit = when (navEvent) {
    NavigateUp -> {
        navController.navigateUp().let { Unit }
    }
    StartOver -> {
        // Cancel all job for processScope
        processScope?.coroutineContext?.cancelChildren()
        requireActivity().restart()
    }
    is PopBackStack -> {
        navController.popBackStack(
            navEvent.destinationId,
            navEvent.inclusive
        ).let { Unit }
    }
    is NavCommand -> {
        navController.navigate(navEvent.action, navEvent.args, navEvent.navOptions)
    }
    is GlobalNavCommand -> {
        navigateGlobal(navEvent)
    }
}

fun Fragment.navigateGlobal(navCommand: GlobalNavCommand) {
    val navController = if (navCommand.globalHost == null) {
        findNavController()
    } else {
        Navigation.findNavController(requireActivity(), navCommand.globalHost)
    }
    navController.navigate(navCommand.action, navCommand.args, navCommand.navOptions)
}