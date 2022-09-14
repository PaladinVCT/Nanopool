package by.lebedev.nanopoolmonitoring.ui.mainscreen

import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.lebedev.nanopoolmonitoring.BuildConfig
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.databinding.ActivityMainScreenBinding
import by.lebedev.nanopoolmonitoring.domain.premium.CheckPremiumUseCase
import by.lebedev.nanopoolmonitoring.utils.*
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@AndroidEntryPoint
class MainScreenActivity : AppCompatActivity() {

    private var countDownTimer: CountDownTimer
    var isCountDownTimerRunning: Boolean = false

    @Inject
    lateinit var sp: SharedPreferences

    @Inject
    lateinit var checkPremiumUseCase: CheckPremiumUseCase

    private var mRewardedAd: RewardedAd? = null

    var binding: ActivityMainScreenBinding? = null

    var limitDialog: MaterialDialog? = null

    init {
        countDownTimer = object : CountDownTimer(BuildConfig.NO_ADS_GRACE_INTERVAL, 1_000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                isCountDownTimerRunning = false
                showLimitReachedDialog(premiumClickedCallback = {
                    limitDialog = it
                }) {
                    sp.setNoAdsInternal()
                    it.dismiss()
                    limitDialog=null
                    this@MainScreenActivity.binding?.root?.let {
                        Snackbar.make(
                            it,
                            R.string.you_unlock_for_now,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navView: BottomNavigationView? = binding?.navView

        val navHostFragment =
            binding?.navHostFragmentActivityMainScreen?.id?.let {
                supportFragmentManager.findFragmentById(
                    it
                )
            }
                    as NavHostFragment
        val navController = navHostFragment.navController

        navView?.setupWithNavController(navController)
    }

    private fun prepareRewardedAds() {
        val rewardedAdRequest = AdRequest.Builder().build()

        RewardedAd.load(
            this,
            BuildConfig.REWARDER_ID,
            rewardedAdRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                }
            })
    }

    fun showRewardedAd(callback: (Boolean) -> Unit) {
        if (mRewardedAd != null) {
            mRewardedAd?.show(this) {
                callback(true)
            }
        } else {
            callback(false)
        }
    }

    override fun onResume() {
        if (SessionBearer.wallet == null)
            finish()

        prepareRewardedAds()

        limitDialog?.let { checkPremium(it) }
        super.onResume()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    fun checkPremiumAndStartAdsTimer() {
        if (!sp.checkPremium() && sp.isAdsInterval() && limitDialog == null) {
            startAdsCountDownTimer()
        }
    }

    private fun startAdsCountDownTimer() {
        if (!isCountDownTimerRunning) {
            countDownTimer.start()
            isCountDownTimerRunning = true
        }
    }

    private fun checkPremium(dialog: MaterialDialog) {
        checkPremiumUseCase.invoke()
            .trackLoading {}
            .trackError {}
            .trackSuccess {
                delay(500)
                SessionBearer.isPremium = sp.checkPremium()
                if (sp.checkPremium()) {
                    dialog.dismiss()
                    limitDialog = null
                    this@MainScreenActivity.binding?.root?.let { it1 ->
                        Snackbar.make(
                            it1,
                            R.string.you_are_now_premium,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .launchIn(lifecycleScope)
    }
}