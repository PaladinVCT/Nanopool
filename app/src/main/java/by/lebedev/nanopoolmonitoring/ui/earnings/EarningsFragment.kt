package by.lebedev.nanopoolmonitoring.ui.earnings

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.databinding.FragmentDashboardBinding
import by.lebedev.nanopoolmonitoring.databinding.FragmentEarningsBinding
import by.lebedev.nanopoolmonitoring.databinding.FragmentWorkersBinding
import by.lebedev.nanopoolmonitoring.ui.BindingInflater
import by.lebedev.nanopoolmonitoring.ui.NanopoolFragment
import by.lebedev.nanopoolmonitoring.ui.editwallet.EditWalletController
import by.lebedev.nanopoolmonitoring.ui.editwallet.EditWalletViewModel
import by.lebedev.nanopoolmonitoring.ui.mainscreen.MainScreenActivity
import by.lebedev.nanopoolmonitoring.ui.options.OptionsFragment.Companion.SELECTED_FIAT
import by.lebedev.nanopoolmonitoring.utils.CoinTicker
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.Input
import by.lebedev.nanopoolmonitoring.utils.setupOnBackPressed
import com.afollestad.materialdialogs.utils.MDUtil.getStringArray
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.epoxy.VisibilityState
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class EarningsFragment : NanopoolFragment<FragmentEarningsBinding>() {

    @Inject
    lateinit var controller: EarningsController

    override val viewModel: EarningsViewModel by fragmentViewModel()

    override val bindingInflater: BindingInflater<FragmentEarningsBinding> =
        FragmentEarningsBinding::inflate

    override fun invalidate() = withState(viewModel) { state ->
        requireBinding().state = state
        controller.setData(state)
    }

    override fun onViewCreated(binding: FragmentEarningsBinding, savedInstanceState: Bundle?) {
        setupOnBackPressed(viewLifecycleOwner,sp)

        sp.getString(SELECTED_FIAT, requireContext().getStringArray(R.array.fiatCurrency)[0])
            ?.let {
                viewModel.setGlobalSelectedFiat(
                    requireContext().getStringArray(R.array.fiatCurrency).indexOf(it))
            }

        withBinding {
            withState(viewModel) {
                state = it
            }
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
                        requireActivity().finish()
                        true
                    }
                    else -> true
                }
            }
            controller.callbacks = controllerCallbacks
            earningsRv.setController(controller)
        }

        viewModel.onEach(
            EarningsViewState::enteredHashrate
        ) {
            viewModel.loadApproximatedEarnings(it.toDoubleOrNull() ?: 0.0, true)
        }
        viewModel.init()
    }

    private val controllerCallbacks = object : EarningsController.Callbacks {
        override fun onCoinPriceFiatSelected(position: Int) {
            viewModel.onCoinPriceFiatSelected(requireContext().getStringArray(R.array.fiatCurrency)[position])

        }

        override fun onEarningsFiatSelected(position: Int) {
            viewModel.onFiatSelected(requireContext().getStringArray(R.array.fiatCurrency)[position])
        }

        override fun onAthFiatSelected(position: Int) {
            viewModel.onAthFiatSelected(requireContext().getStringArray(R.array.fiatCurrency)[position])
        }

        override fun onAthChangeFiatSelected(position: Int) {
            viewModel.onAthChangeFiatSelected(requireContext().getStringArray(R.array.fiatCurrency)[position])
        }

        override fun onCalculatorFiatSelected(position: Int) {
            viewModel.onCalculatorFiatSelected(requireContext().getStringArray(R.array.fiatCurrency)[position])
        }

        override fun onCalculatorClick() {
            viewModel.onCalculatorClick()
        }

        override fun onHashrateChange(input: Input) {
            viewModel.onHashrateChange(input)
        }

    }

    override fun onResume() {
        (requireActivity() as MainScreenActivity).binding?.navView?.isVisible = true
        (requireActivity() as MainScreenActivity).checkPremiumAndStartAdsTimer()
        super.onResume()
    }

}