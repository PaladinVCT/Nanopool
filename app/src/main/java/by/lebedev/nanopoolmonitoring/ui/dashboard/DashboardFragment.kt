package by.lebedev.nanopoolmonitoring.ui.dashboard

import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.view.isVisible
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.databinding.FragmentDashboardBinding
import by.lebedev.nanopoolmonitoring.ui.BindingInflater
import by.lebedev.nanopoolmonitoring.ui.NanopoolFragment
import by.lebedev.nanopoolmonitoring.ui.mainscreen.MainScreenActivity
import by.lebedev.nanopoolmonitoring.ui.options.OptionsFragment
import by.lebedev.nanopoolmonitoring.utils.*
import com.afollestad.materialdialogs.utils.MDUtil.getStringArray
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : NanopoolFragment<FragmentDashboardBinding>() {

    @Inject
    lateinit var controller: DashboardController

    override val viewModel: DashboardViewModel by fragmentViewModel()

    override val bindingInflater: BindingInflater<FragmentDashboardBinding> =
        FragmentDashboardBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.init()
        super.onCreate(savedInstanceState)
    }

    override fun invalidate() = withState(viewModel) { state ->
        requireBinding().state = state
        controller.setData(state)
    }

    override fun onViewCreated(binding: FragmentDashboardBinding, savedInstanceState: Bundle?) {
        setupOnBackPressed(viewLifecycleOwner,sp)
        withBinding {
            withState(viewModel) {
                state = it
            }
            controller.callbacks = controllerCallbacks
            dashboardRv.setController(controller)
            swRefresh.setOnRefreshListener {
                viewModel.init()
                swRefresh.isRefreshing = false
            }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.options -> {
                        viewModel.onOptionsClick()
                        true
                    }
                    R.id.logout -> {
                        sp.logout()
                        requireActivity().finish()
                        true
                    }
                    else -> true
                }
            }
            val epoxyVisibilityTracker = EpoxyVisibilityTracker()
            epoxyVisibilityTracker.attach(dashboardRv)
        }

        sp.getString(
            OptionsFragment.SELECTED_FIAT,
            requireContext().getStringArray(R.array.fiatCurrency)[0]
        )
            ?.let { viewModel.setGlobalSelectedFiat(it) }

        viewModel.onEach(
            DashboardViewState::payoutLimit,
            DashboardViewState::balance,
            DashboardViewState::approximatedEarnings,
        ) { payoutLimit, balance, aprx ->
            if (payoutLimit != null && balance != null && aprx != null)
                viewModel.setTimeToNextPayoutRes(
                    payoutLimit.payout,
                    balance.balance,
                    aprx.earnings,
                    requireContext()
                )
        }
    }

    override fun onResume() {
        if (sp.getBoolean(OptionsFragment.AUTO_UPDATE_ENABLED, false)) viewModel.startAutoUpdate()
        (requireActivity() as MainScreenActivity).binding?.navView?.isVisible = true
        (requireActivity() as MainScreenActivity).checkPremiumAndStartAdsTimer()
        super.onResume()
    }

    override fun onStop() {
        viewModel.stopAutoUpdate()
        super.onStop()
    }

    private val controllerCallbacks = object : DashboardController.Callbacks {
        override fun onPayoutProgressVisibilityChanged(visibilityState: Int) {
            viewModel.onPayoutProgressVisibilityChanged(visibilityState)
        }
    }
}