package by.lebedev.nanopoolmonitoring.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import by.lebedev.nanopoolmonitoring.BuildConfig
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.utils.*
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import javax.inject.Inject

typealias BindingInflater<VB> = (LayoutInflater, ViewGroup?, Boolean) -> VB

abstract class NanopoolFragment<V : ViewDataBinding> : Fragment(), MavericksView {

    protected var mRewardedAd: RewardedAd? = null

    @Inject
    lateinit var sp: SharedPreferences

    @Inject
    lateinit var processScope: CoroutineScope

    private var binding: V? = null
    private lateinit var navController: NavController
    abstract val bindingInflater: BindingInflater<V>
    protected abstract val viewModel: NanopoolViewModel<out MavericksState>

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return createBinding(inflater, container, savedInstanceState).also {
            it.lifecycleOwner = viewLifecycleOwner
            binding = it
        }.root
    }

    protected open fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): V = bindingInflater(inflater, container, false)

    abstract fun onViewCreated(binding: V, savedInstanceState: Bundle?)

    protected fun requireBinding(): V = requireNotNull(binding)
    protected fun requireBaseContext(): Context = requireActivity().baseContext
    protected inline fun withBinding(block: V.() -> Unit) = requireBinding().block()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        onViewCreated(requireBinding(), savedInstanceState)

//        val rewardedAdRequest = AdRequest.Builder().build()
//
//        RewardedAd.load(
//            requireContext(),
//            BuildConfig.REWARDER_ID,
//            rewardedAdRequest,
//            object : RewardedAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    mRewardedAd = null
//                }
//
//                override fun onAdLoaded(rewardedAd: RewardedAd) {
//                    mRewardedAd = rewardedAd
//                }
//            })

        viewModel.onNavigateEvent.observeEvent(viewLifecycleOwner) {
            handleNavigation(it)
        }
        viewModel.onErrorEvent.observeEvent(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.onMessageResEvent.observeEvent(viewLifecycleOwner) {
            handleMessageRes(it)
        }
    }

    open fun handleError(throwable: Throwable) {
        handleDefaultError(throwable)
    }

    open fun handleNavigation(navEvent: BaseNavEvent): Unit =
        handleDefaultNavigation(
            navController = navController,
            navEvent = navEvent,
            processScope = processScope
        )

    open fun handleMessageRes(stringRes: Int) {
        longToast(stringRes)
    }

//    fun showRewardedAd(callback: (Boolean) -> Unit) {
//        if (mRewardedAd != null) {
//            mRewardedAd?.show(requireActivity()) {
//                callback(true)
//            }
//        } else {
//            callback(false)
//        }
//    }
}